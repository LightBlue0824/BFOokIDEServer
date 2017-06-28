//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {
	private int memorySize = 1024;
	private boolean isInfinite = false;				//记录数组单元是否是有限循环的
	/**
	 * 请实现该方法
	 */
	@Override
	public String executeBF(String code, String param) throws RemoteException {
		char[] memory = new char[memorySize];			//内存
		int ptr = 0;				//指针
		
		int lineCount = 1;			//行号
		int columnCount = 0;				//列号
		
		String result = "";				//保存结果
		char[] perCode = code.toCharArray();		//每个代码
		char[] perParam = param.toCharArray();		//每个参数
		int paramIndex = 0;
		
		//循环执行代码
		int position = 0;			//记录运行到的位置
		while(position < perCode.length){
			columnCount++;
			switch(perCode[position]){
			case '>':{			//指针加一
				ptr++;
				if(ptr >= memorySize){		//判断是否上溢
					if(isInfinite){
						ptr = 0;
					}
					else{
						return "Error! Memory overflow. Index: "+ptr;
					}
				}
				break;
			}
			case '<':{			//指针减一
				ptr--;
				if(ptr < 0){		//判断是否下溢
					if(isInfinite){
						ptr = memorySize-1;
					}
					else{
						return "Error! Memory overflow. Index: "+ptr;
					}
				}
				break;
			}
			case '+':{			//指针指向的单元内容加一
				memory[ptr]++;
				break;
			}
			case '-':{			//指针指向的单元内容减一
				memory[ptr]--;
				break;
			}
			case '.':{			//输出指针指向的单元内容（ASCII码）
				result = result + memory[ptr];
				break;
			}
			case ',':{
				//System.out.println(paramIndex +" "+ perParam.length);

				if(paramIndex >= perParam.length){				//参数个数不足
					return "Error! The parameters are not enough. ";
				}
				memory[ptr] = perParam[paramIndex];
				paramIndex++;
				break;
			}
			case '[':{
				int tempIndex = -1;
				
				//找出对应的']'
				int count = 0;
				for(int i = position+1;i < perCode.length && count >= 0;i++){
					if(perCode[i] == '['){			//遇到'['说明里面这个循环里有其他循环,count++
						count++;
					}
					if(perCode[i] == ']'){			//遇到']'，某个循环的终点，count--;
						//若不为0则是该循环里的其他循环的终点
						if(count == 0){				//若count为0,说明是这个循环的终点
							tempIndex = i;
							break;
						}
						count--;
					}
				}
				//判断是否找到对应的']'
				if(tempIndex == -1){
					return "Error! No matched ']'. ";
				}
				
				//判断指针指向的单元是否为0,即是否需要跳转
				if(memory[ptr] == 0){
					position = tempIndex;
				}
				break;
			}
			case ']':{
				int tempIndex = -1;
				
				//找出对应的'['
				int count = 0;
				for(int i = position-1;i >= 0 && count >= 0;i--){
					if(perCode[i] == ']'){			//若遇到']'，说明该循环里还有其他循环，count++
						count++;
					}
					if(perCode[i] == '['){			//若遇到'[',某个循环的起始点。count--;
						//若count不为0，说明是其他循环的起始点
						if(count == 0){				//若count为0，说明是该循环的起始点
							tempIndex = i;
							break;
						}
						count--;
					}
				}
				//判断是否找到对应的'['
				if(tempIndex == -1){
					return "Error! No matched '['. ";
				}
				
				//判断指针指向的单元是否为0，即是否需要跳转
				if(memory[ptr] != 0){
					position = tempIndex;
				}
				break;
			}
			case '\n':{
				lineCount++;
				columnCount = 0;				//清0列标记
			}
			case ' ':{
				//nothing
			}
			default:{
				return "Error!  Illegal instructions. At line "+lineCount+" column "+columnCount;
			}
			}//switch结束
			
			position++;				//指令位置加一
		}
		return result;
	}
	
	@Override
	public String executeOok(String code, String param) throws RemoteException {
		String[] perLine = code.split("\n");
		
		String bfCode = "";
		for(int i = 0;i < perLine.length;i++){
			String[] perCode = perLine[i].split(" ");
			
			//判断是否是奇数
			if(perCode.length%2 == 1){
				return "Error! Illegal instructions. At line "+(i+1);
			}
			
			for(int j = 0;j < perCode.length;j = j+2){
				if(perCode[j].equals("Ook.")){
					if(perCode[j+1].equals("Ook?")){		//Ook. Ook? 
						bfCode += '>';
					}
					else if(perCode[j+1].equals("Ook.")){		//Ook. Ook. 
						bfCode += '+';
					}
					else if(perCode[j+1].equals("Ook!")){		//Ook. Ook! 
						bfCode += ',';
					}
					else{
						return "Error! Illegal instruction. At line "+(i+1)+" No."+(j+2);
					}
				}
				else if(perCode[j].equals("Ook?")){
					if(perCode[j+1].equals("Ook.")){			//Ook? Ook. 
						bfCode += '<';
					}
					else if(perCode[j+1].equals("Ook!")){		//Ook? Ook! 
						bfCode += ']';
					}
					else{
						return "Error! Illegal instruction. At line "+(i+1)+" No."+(j+2);
					}
				}
				else if(perCode[j].equals("Ook!")){
					if(perCode[j+1].equals("Ook!")){			//Ook! Ook! 
						bfCode += '-';
					}
					else if(perCode[j+1].equals("Ook.")){		//Ook! Ook. 
						bfCode += '.';
					}
					else if(perCode[j+1].equals("Ook?")){		//Ook! Ook? 
						bfCode += '[';
					}
					else{
						return "Error! Illegal instruction. At line "+(i+1)+" No."+(j+2);
					}
				}
				else{
					return "Error! Illegal instructions. At line "+(i+1)+" No."+(j+1);
				}
			}
		}
		return executeBF(bfCode, param);
	}
	
	@Override
	public void setMemorySize(int size) throws RemoteException {
		memorySize = size;
	}
	
	@Override
	public int getMemorySize() throws RemoteException {
		return memorySize;
	}
	
	@Override
	public void setInfinite(boolean value){
		isInfinite = value;
	}
	
	@Override
	public boolean isInfinite(){
		return isInfinite;
	}
}
