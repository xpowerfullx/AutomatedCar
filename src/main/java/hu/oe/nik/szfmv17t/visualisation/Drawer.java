package hu.oe.nik.szfmv17t.visualisation;

import hu.oe.nik.szfmv17t.environment.domain.Turn;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldVisualisation;
import hu.oe.nik.szfmv17t.visualisation.interfaces.IWorldVisualization;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mariusz on 2017.03.15..
 */
public class Drawer implements IWorldVisualization {
    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }
    private IWorldVisualization world;
    @Override
    public List<IWorldObject> getWorld() {
        return world.getWorld();
    }
    private static Drawer instance = null;
    private static ArrayList <BufferedImage> worldImages;
    private Drawer(IWorldVisualisation world)
    {}

    public static Drawer getDrawer(IWorldVisualisation world) throws IOException {
        if (instance==null) {
            worldImages=new ArrayList<BufferedImage>();
            instance = new Drawer(world);
            for (IWorldObject object:world.getWorld()) {
                BufferedImage bimg = ImageIO.read(new File(ClassLoader.getSystemResource(object.getImageName()).getFile()));
                worldImages.add(bimg);
            }
        }
        return instance;
    }

    public FrameComposer getComposer(IWorldVisualisation world)
    {
        return FrameComposer.getComposer(world);
    }

    //private static double t=0.1;
    //double direction =0.1;
    int t=0;
    public void DrawFrametoPanel(JPanel worldObjectsPanel, IWorldVisualisation world, JPanel mainPanel)
    {
        BorderLayout layout = (BorderLayout)mainPanel.getLayout();
        mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));;
        FrameComposer fc = getComposer(world);
        fc.setCameraSize(worldObjectsPanel.getWidth(),worldObjectsPanel.getHeight());
        List<IWorldObject> toDraw=fc.composeFrame();

        worldObjectsPanel = new JPanel() {
            private static final long serialVersionUID = 1L;
            public void paintComponent(Graphics g) {
                int t2=0;

                BufferedImage image;
                Graphics2D g2d=(Graphics2D)g.create();
                for (IWorldObject object : toDraw) {
                    // draw objects
                    image = worldImages.get(t2++);

                    double drawCornerX=0;
                    double drawCornerY=(int)(object.getCenterY()-object.getHeight()/2);
                    if (Turn.class.isInstance(object))
                    {
                        drawCornerY=(int)(object.getCenterY()-object.getHeight()*1.5);
                        switch (object.getImageName()) {
                            case "road_2lane_90left.png":
                                //drawCornerX=(int)(object.getCenterX()-(object.getWidth()+(object.getWidth()-350)));
                                drawCornerX = (int) (object.getCenterX() - (object.getWidth()/2 + (object.getWidth() - 350)));
                                break;
                            case "road_2lane_90right.png":
                                drawCornerX=(int)(object.getCenterX()-(object.getWidth()/2+350));
                                break;
                            case "road_2lane_45left.png":
                                drawCornerX=(int)(object.getCenterX()-(object.getWidth()+(object.getWidth()-350)));
                                break;
                            case "road_2lane_45right.png":
                                drawCornerX=(int)(object.getCenterX()-(object.getWidth()/2+350));
                                break;
                        }
                    }
                    else {
                        drawCornerX = ((int) (object.getCenterX() - object.getWidth() / 2)) ;
                        //drawCornerY = ((int) (object.getCenterY() - object.getHeight() / 2)) ;
                    }
                    PutDebugInformationOnImage(image, object);
                    //AffineTransform transform = new AffineTransform();
                    //drawCornerX=drawCornerX/(1d/t);
                    //drawCornerY=drawCornerY/(1d/t);
                    AffineTransform fullTransForm=new AffineTransform();
                    fullTransForm.rotate(-object.getAxisAngle());

                    double[] pt = {drawCornerX, drawCornerY};
                    AffineTransform.getRotateInstance(object.getAxisAngle(), drawCornerX+object.getWidth()/2, drawCornerY+object.getHeight()/2)
                            .transform(pt, 0, pt, 0, 1); // specifying to use this double[] to hold coords
                    drawCornerX = pt[0];
                    drawCornerY = pt[1];
                    fullTransForm.translate(drawCornerX,drawCornerY);

                    //AffineTransform tr=fullTransForm.getTranslateInstance(drawCornerX,drawCornerY);
                    //AffineTransform transform = AffineTransform.getTranslateInstance(drawCornerX, drawCornerY);
                    //transform.rotate(-object.getAxisAngle());
                    //AffineTransform transform2 = AffineTransform.getTranslateInstance(drawCornerX, drawCornerY);
                    g2d.drawImage(image,fullTransForm, null);
                    //DEBUG OVERLAY
                }
                t+=5;
            }
        };
        mainPanel.add(worldObjectsPanel,BorderLayout.CENTER);
    }
    private void PutDebugInformationOnImage (Image image, IWorldObject object) {
        Graphics2D g = (Graphics2D) image.getGraphics();

        String loc = String.format ("x: %.0f, y:%.0f", object.getCenterX(), object.getCenterY(), object.getAxisAngle());
        String rot = String.format ("%.3f (rad)", object.getAxisAngle());

        g.setColor(Color.red);
        g.drawRect(0, 0, image.getWidth(null) - 1, image.getHeight(null) - 1);

        g.setColor(Color.black);
        g.setFont(new Font("sans", Font.PLAIN, 15));
        g.drawString(loc, 3, 20);
        g.drawString(rot, 3, 35);
    }
}
