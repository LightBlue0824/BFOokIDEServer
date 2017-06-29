package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import service.ExecuteService;
import service.IOService;
import service.UserService;
import serviceImpl.ExecuteServiceImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService, ExecuteService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private IOService iOService;
	private UserService userService;
	private ExecuteService executeService;
	
	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeService = new ExecuteServiceImpl();
	}

	@Override
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.writeFile(file, userId, fileName);
	}

	@Override
	public String readFile(String userId, String fileName) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFile(userId, fileName);
	}

	@Override
	public String readHistoryVersion(String userId, String fileName, String version) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readHistoryVersion(userId, fileName, version);
	}

	@Override
	public String[] readFileList(String userId) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFileList(userId);
	}

	@Override
	public boolean deleteFile(String userId, String fileName) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.deleteFile(userId, fileName);
	}
	
	@Override
	public boolean renameFile(String userId, String oldFileName, String newFileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.renameFile(userId, oldFileName, newFileName);
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.logout(username);
	}

	@Override
	public boolean register(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.register(username, password);
	}

	@Override
	public boolean delete(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.delete(username);
	}
	
	@Override
	public String executeBF(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.executeBF(code, param);
	}
	
	@Override
	public String executeOok(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.executeOok(code, param);
	}
	
	@Override
	public void setMemorySize(int size) throws RemoteException{
		// TODO Auto-generated method stub
		executeService.setMemorySize(size);
	}

	@Override
	public int getMemorySize() throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.getMemorySize();
	}

	@Override
	public void setInfinite(boolean value) throws RemoteException {
		// TODO Auto-generated method stub
		executeService.setInfinite(value);
	}

	@Override
	public boolean isInfinite() throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.isInfinite();
	}

}
