

package bouncingsprites;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SpritePanel extends JPanel{
	Sprite sprite;
	ExecutorService pool = Executors.newCachedThreadPool();
	
	public SpritePanel(){
		addMouseListener(new Mouse());
	}
	
	private void newSprite (MouseEvent event){
		sprite = new Sprite(this);
		pool.execute(sprite);
		System.out.println("New ball created");
		/* OLD
		sprite = new Sprite(this);
		System.out.println("New ball created");
		*/
	}
	
	public void animate(){
		/* OLD
	    while (true){
	    	
	        if (sprite != null){
		        sprite.move();  
	        }
	        //invokes paintComponent
	        repaint();
 	        //sleep while waiting to display the next frame of the animation
	        try {
	            Thread.sleep(40);  // wake up roughly 25 frames per second
	        }
	        catch ( InterruptedException exception ) {
	            exception.printStackTrace();
	        }
	    }
	    */
	}
	
	private class Mouse extends MouseAdapter {
		@Override
	    public void mousePressed( final MouseEvent event ){
	        newSprite(event);
	    }
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(sprite!=null){
			sprite.draw(g);
		}
	}
}
