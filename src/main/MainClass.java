package main;

public class MainClass
{
	
	public static void main(String[] args) 
	{
		//This is main class of the Adventure Ball Game 
		//This is where the Game will be initiated from
		
		
		System.setProperty("sun.java2d.opengl", "true");
		//This is a property I added makes animations smoother
		
		new Game();
	}

}
