
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;



public class Main extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1489534897L;

	private int[] pow, co;
	private String[] sign;

	private int width=1280,height=720;
	private Thread thread;
	private Graphics2D g;
	private BufferedImage image;

	public Main() 
	{
		super();
		setPreferredSize(new Dimension(width, height));
		this.setFocusable(true);
		requestFocus();
	}

	public void init()
	{
		image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		co = new int[1];
		pow = new int[1];
		sign = new String[1];
		co[0]=1;
		pow[0]=1;
		sign[0]="+";

	}

	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run()
	{
		init();
		while(true)
		{
			update();
			draw();
			render();
			g.clearRect(0,0,width,height);
		}
	}

	public void update()
	{
	}
	public void render()
	{
		long start = System.nanoTime();
		int prevX=0;
		int prevY=0;
		int iterator=0;
		int xVals = 9;
		double xScale=1;
		int size=7;

		double scale=10.0;
		boolean skip=false;
		for(int i=1;i<20;i++)
		{
			
			if(!skip)
			{
				g.setColor(new Color(255,255,255,255));
				g.drawString("White:   Taylor Series",0, 10);
				g.setColor(new Color(255,0,0,255));
				g.drawString("Red:   Sine Wave",0, 23);
				g.setColor(new Color(0,200,0,255));
				g.drawString("Approximation: "+((i/2)+1),width-100, 10);
				skip=true;

				System.out.println("approximation "+i);
				prevX=0;
				prevY=0;
				
				iterator=-xVals;
				for(double j=-xVals;j<xVals;j+=xScale/40)
				{
					if(prevX>width-50)
					{
						prevX=0;
						iterator=0;
						g.clearRect(0, 0, width, height);
					}

					double temp = calcSinXSeries(j,i);
					
					System.out.println(Math.abs(Math.sin(j)-temp));
					int pos=0;
					if(temp<0 && temp>-1)
					{
						pos=(int)(temp*scale);

					}
					else if(temp<1 && temp>0)
					{
						pos=(int)(temp*scale);
					}
					else
					{
						pos=(int)(temp);
					}
					//System.out.println(pos);
					
					g.setColor(new Color(255,255,255,50));
					g.drawOval((int)(250+iterator*xScale),(height>>1) + pos,size,size);
					g.setColor(new Color(255,0,0,75));
					g.drawOval((int)(250+iterator*xScale),(height>>1) + (int)(Math.sin(j)*scale),size,size);

					prevX=(int)(iterator*xScale);
					prevY=pos;
					delay((int)(xScale*1),true);
					draw();
					iterator++;
				}
				if(i==19){delay(10000,true);}
				delay(500,true);
				g.clearRect(0,0,width,height);
				System.out.println("");
				
			}
			else if(skip){skip=false;}
		}
		delay(1000,true);
		//System.out.println((System.nanoTime()-start)/1000000+" millis ");
	}
	public void draw()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image,0,0,this);
		g2.dispose();
	}

	public double calcSinXSeries(double x, int iterations)
	{
		double result=x;
		boolean flip=true;
		boolean skip=false;
		for(int i=1;i<iterations;i++)
		{
			if(!skip)
			{
				skip=true;
				if(!flip)
				{
					result+=(double)((Math.pow(x, i+2))/calcFactorial(i+2));
					flip=true;
				}
				else
				{
					result-=(double)((Math.pow(x, i+2))/calcFactorial(i+2));
					flip=false;
				}
			}
			else if(skip){skip=false;}
		}

		//System.out.println(result);
		return result;
	}
	public long calcFactorial(int x)
	{
		long result=1L;
		for(long i=1;i<=x;i++){result*=i;}
		return result;
	}

	public int computeDerivative(int[] coeffecient, int[] exponent, String[] signs, int x)
	{
		int[]xVals = new int[exponent.length];

		for(int i=0;i<xVals.length;i++)
		{
			xVals[i] = (int) Math.pow((coeffecient[i] * exponent[i] * x),  exponent[i]-1);
		}

		int result=xVals[0];

		for(int i=1;i<xVals.length;i++)
		{
			if(signs[i].equals("-"))
			{
				result-=xVals[i];
			}
			else if(signs[i].equals("+"))
			{
				result+=xVals[i];
			}
		}

		return result;
	}

	public void delay(int millis, boolean draw)
	{
		try
		{
			if(draw)
			{
				draw();
			}
			Thread.sleep(millis);
		}
		catch(Exception e){e.printStackTrace();}
	}

	public static void main(String[] args)
	{

		JFrame window = new JFrame("Main");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new Main());
		window.setBackground(Color.BLACK);
		window.pack();
		window.setVisible(true);
	}

}
