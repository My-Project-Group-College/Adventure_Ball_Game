package main;

public class MainClass
{
	
	public static void main(String[] args) 
	{
		//This is main class of the Adventure Ball Game 
		//This is where the Game will be initiated from
		
//		if(System.getProperty("os.name").equals("Linux"))
//			System.setProperty("sun.java2d.opengl", "true");
		//This is a property I added makes animations smoother in Linux but for some reason it makes the game crash will further test
		
		new Game();
	}

}
