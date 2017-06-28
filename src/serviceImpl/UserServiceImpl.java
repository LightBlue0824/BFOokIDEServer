package serviceImpl;

import java.io.*;
import java.rmi.RemoteException;

import service.UserService;

public class UserServiceImpl implements UserService{

	//private ArrayList<String> users = new ArrayList<String>();			//��¼��ǰ��¼���û�
	
	//�û��б�,��¼�û���Ϣ
	File userList = new File("users/UserList.txt");

	@Override
	public boolean login(String username, String password) throws RemoteException {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(userList));
			
			while(bf.ready()){
				if(bf.readLine().equals(username+","+password)){
					bf.close();
					return true;
				}
			}
			bf.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return true;
	}

	@Override
	public boolean register(String username, String password) throws RemoteException {
		try {
			//���ж��Ƿ����Ѵ��ڵ��û�
			BufferedReader br = new BufferedReader(new FileReader(userList));
			
			while(br.ready()){
				if(br.readLine().indexOf(username) != -1){
					br.close();
					return false;					//���û����Ѵ���
				}
			}
			br.close();
			
			//Ȼ�����û�����Ϣд���û��б�
			FileWriter fw = new FileWriter(userList, true);
			fw.write(username +","+ password + System.lineSeparator());
			fw.flush();
			fw.close();
			
			//�½����û����ļ���
			File folder = new File("users/"+username);
			folder.mkdir();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
	
	@Override
	public boolean delete(String username) throws RemoteException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(userList));
			
			String afterDelete = "";
			
			while(br.ready()){
				String perLine = br.readLine();
				String[] userInfo = perLine.split(",");
				if(userInfo != null && !userInfo[0].equals(username)){
					afterDelete += perLine + System.lineSeparator();
				}
			}
			br.close();
			
			FileWriter fw = new FileWriter(userList);
			fw.write(afterDelete);
			fw.flush();
			fw.close();
			
			File folder = new File("users/"+username);
			deleteDir(folder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
	
	//�ݹ�ɾ�������ļ���
	private void deleteDir(File file){
		if(!file.exists()){
			return;
		}
		if(file.delete()){
			return;
		}
		File[] files = file.listFiles();
		
		for(int i = 0;i < files.length;i++){
			deleteDir(files[i]);
		}
		file.delete();
	}
}
