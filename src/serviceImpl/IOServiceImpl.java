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
		//����Ŀǰ�汾
		File directory = new File("users/"+userId + "/" + fileName);
		directory.mkdir();
		
		File fileCurrent = new File("users/"+userId + "/" + fileName +"/"+ fileName);
		
		try {
			FileWriter fw = new FileWriter(fileCurrent, false);
			fw.write(file);
			fw.flush();
			fw.close();
			
			//��¼Ϊ��ʷ�汾
			saveAsHistoryFile(file, userId, fileName);
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	//����ǰ������ļ���¼Ϊ��ʷ�汾
	private void saveAsHistoryFile(String file, String userId, String fileName){
		//��Ҫ��¼Ϊ��ʷ�汾
		
		//��ȡ��ǰ��ʱ��
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmSSS");
		String date = dateFormat.format(new Date());
		
		//������ʷ�汾�����Ӻ�׺������������
		//��ʷ�汾��Ŀ¼
		File directory = new File("users/"+userId + "/" + fileName +"/history");
		directory.mkdir();
		
		File fileHistory = new File("users/"+userId + "/" + fileName +"/history/" + date);

		try {
			FileWriter fw = new FileWriter(fileHistory, false);
			fw.write(file);
			fw.flush();
			fw.close();
			
			//�õ�Ŀ¼�ļ��б�
			String[] filenameList = directory.list();
			
			if(filenameList.length > 10){			//���ֻ�ܱ���10���汾
				//ɾ����ǰ�İ汾
				long[] dateNum = new long[filenameList.length];
				
				//�ҳ���С��һ��
				long min = Long.parseLong(filenameList[0]);
				for(int i = 0; i < filenameList.length;i++){
					dateNum[i] = Long.parseLong(filenameList[i]);
					if(dateNum[i] < min){
						min = dateNum[i];
					}
				}
				
				//ɾ��10���汾������İ汾
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
		
		String code = "";			//�����������
		
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
		
		String code = "";			//�����������
		
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
		//Ŀ¼
		File directory = new File("users/"+userId);
		
		//�����ļ��б�
		return directory.list();
	}

	@Override
	public boolean deleteFile(String userId, String fileName) throws RemoteException {
		//ɾ���ļ�������������ʷ�汾
		File file = new File("users/"+userId+"/"+fileName);
		
		return deleteDir(file);
	}
	
	//�ݹ�ɾ�������ļ���
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
