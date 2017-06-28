package serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import service.IOService;

public class IOServiceImpl implements IOService{
	
	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		//保存目前版本
		File directory = new File("users/"+userId + "/" + fileName);
		directory.mkdir();
		
		File fileCurrent = new File("users/"+userId + "/" + fileName +"/"+ fileName);
		
		try {
			FileWriter fw = new FileWriter(fileCurrent, false);
			fw.write(file);
			fw.flush();
			fw.close();
			
			//记录为历史版本
			saveAsHistoryFile(file, userId, fileName);
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	//将当前保存的文件记录为历史版本
	private void saveAsHistoryFile(String file, String userId, String fileName){
		//需要记录为历史版本
		
		//获取当前的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmSSS");
		String date = dateFormat.format(new Date());
		
		//对于历史版本，不加后缀名，方便排序
		//历史版本的目录
		File directory = new File("users/"+userId + "/" + fileName +"/history");
		directory.mkdir();
		
		File fileHistory = new File("users/"+userId + "/" + fileName +"/history/" + date);

		try {
			FileWriter fw = new FileWriter(fileHistory, false);
			fw.write(file);
			fw.flush();
			fw.close();
			
			//得到目录文件列表
			String[] filenameList = directory.list();
			
			if(filenameList.length > 10){			//最多只能保存10个版本
				//删除最前的版本
				long[] dateNum = new long[filenameList.length];
				
				//找出最小的一个
				long min = Long.parseLong(filenameList[0]);
				for(int i = 0; i < filenameList.length;i++){
					dateNum[i] = Long.parseLong(filenameList[i]);
					if(dateNum[i] < min){
						min = dateNum[i];
					}
				}
				
				//删除10个版本中最早的版本
				File toDelete = new File("users/"+userId + "/" + fileName +"/history/" + min);
				toDelete.delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String readFile(String userId, String fileName) {
		File codeFile = new File("users/"+userId+"/"+fileName+"/"+fileName);
		
		String code = "";			//用来保存代码
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(codeFile));
			
			while(br.ready()){
				code += br.readLine();
				if(br.ready()){
					//code += System.lineSeparator();
					code += '\n';
				}
			}

			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}

	@Override
	public String readHistoryVersion(String userId, String fileName, String version){
		File codeFile = new File("users/"+userId+"/"+fileName+"/"+"history/"+version);
		
		String code = "";			//用来保存代码
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(codeFile));
			while(br.ready()){
				code += br.readLine();
				if(br.ready()){
					//code += System.lineSeparator();
					code += '\n';
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}
	
	@Override
	public String[] readFileList(String userId) {
		//目录
		File directory = new File("users/"+userId);
		
		//返回文件列表
		return directory.list();
	}

	@Override
	public boolean deleteFile(String userId, String fileName) throws RemoteException {
		//删除文件，包括所有历史版本
		File file = new File("users/"+userId+"/"+fileName);
		
		return deleteDir(file);
	}
	
	//递归删除整个文件夹
	private boolean deleteDir(File file){
		if(!file.exists()){
			return true;
		}
		boolean temp = file.delete();
		if(temp){
			return true;
		}
		File[] files = file.listFiles();
		
		for(int i = 0;i < files.length;i++){
			deleteDir(files[i]);
		}
		return file.delete();
	}
}
