
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * 
 * */
public class pushbox extends JFrame
{
	char a;
	static Mypanel mypanel;
    static private JButton buttonrestart,buttonedit,buttonexit,buttonchooselevel,startbutton;
    private Container container;
    MouseAdapter mouseAdapter=new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            mypanel.map.setmap(e.getX()-150,e.getY()-50);
            mypanel.repaint();
        }
    };
	public pushbox(){
		mypanel=new Mypanel();
        mypanel.setBounds(100,0,1000,900);
		this.setSize(1000,900);
		this.setTitle("推箱子  --Oceanal");
		this.setVisible(true);
        //加入一些button
//        container=this.getContentPane();
//        container.setLayout(new FlowLayout());
        buttonrestart= new JButton("重新开始本关");
        buttonrestart.setBounds(0,0,100,30);
        buttonrestart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mypanel.map = new map(mypanel.map.level);
                mypanel.repaint();
                mypanel.requestFocus();

            }
        });
        buttonchooselevel=new JButton("下一关");
        buttonchooselevel.setBounds(0,100,100,30);
        buttonchooselevel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (mypanel.map.level != mypanel.map.levelmax) {
                    mypanel.map = new map(mypanel.map.level + 1);
                } else {
                    mypanel.map = new map(1);
                }
                mypanel.repaint();
                mypanel.requestFocus();
            }
        });
        buttonedit=new JButton("编辑自己的关卡");
        buttonedit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked edit");

                super.mouseClicked(e);
                mypanel.map=new map();
                mypanel.requestFocus();
                mypanel.repaint();
                startbutton=new JButton("开始");
                startbutton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        boolean flag=false;
                        String _s;
                        String[] s={"1","1"};
                        while (!flag) {
                            _s = JOptionPane.showInputDialog(pushbox.this, "输入人的位置 空格分隔xy");
                            s = _s.split(" ");
                            int _m=mypanel.map.mapcode[Integer.parseInt(s[0])][Integer.parseInt(s[1])];
                            if(_m==1||_m==2||_m==4){
                                continue;
                            }else {
                                flag=true;
                            }
                        }
                            mypanel.map.posx = Integer.parseInt(s[0]);
                            mypanel.map.posy = Integer.parseInt(s[1]);
                            mypanel.repaint();
                            mypanel.requestFocus();

                        mypanel.removeMouseListener(mouseAdapter);
                    }
                });
                startbutton.setBounds(0,400,100,30);
                pushbox.this.add(startbutton);
                startbutton.requestFocus();
                mypanel.addMouseListener(mouseAdapter);
            }
        });
        buttonedit.setBounds(0,200,100,30);

        buttonexit=new JButton("退出");
        buttonexit.setBounds(0,300,100,30);
        buttonexit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(pushbox.this,"再见！");
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });



        this.setLayout(null);
		this.addKeyListener(mypanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}
	public static void main(String[] args){
		pushbox pb=new pushbox();
		pb.add(mypanel);
        pb.add(buttonrestart);
        pb.add(buttonedit);
        pb.add(buttonchooselevel);
        pb.add(buttonexit);
        mypanel.requestFocus();
	}
}
//JPanel类实现KeyListener接口可以实现四个键盘接口进行监听
//覆盖paint方法画图
class Mypanel extends JPanel implements KeyListener
{
	int x=0,y=0;
	map map;
	boolean winflag=false;
	public Mypanel()
	{
		setFocusable(true);
		this.addKeyListener(this);
		this.map=new map(1);
	}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_R){
			this.map=new map(this.map.level);
			this.repaint();
		}else{
			if(this.winflag){
				if(this.map.level<this.map.levelmax){
					this.map=new map(this.map.level+1);
					
				}else{
					JOptionPane.showMessageDialog(this,"你全部通关了！将进入第一关！");
					this.map=new map(1);
				}
				this.repaint();
				this.winflag=false;
			}else{
				int keycode = e.getKeyCode();
				//System.out.println("an le "+keycode+"jian");
				this.map.move(keycode);
				this.repaint(0);
				if(this.map.ifyouwin()){
					this.winflag=true;
				}
			}
		}
	}
	public void keyReleased(KeyEvent e)
	{
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public void paint(Graphics g){
		//复写父类的paint的方法，就可以在屏幕上画东西了
		super.paint(g);
		g.drawString("您玩的是第"+this.map.level+"关   第"+this.map.step+" 步", 100, 20);
		this.map.draw(g);
		if(this.winflag){
			g.drawString("您过关了，按任意键开始下一关！", 100,40);
		}
	}
}
class map {
    int[][] mapcode;
    int starty;
    int startx;
    int posx;
    int posy;
    int level;
    int step=0;
    final int levelmax=7;
    //定义地图属性：空地0，墙1，箱子2，目标3，箱子在目标上4
    public map(int i){
        switch (i) {
            case 2:
                this.mapcode=new int[][]{{1,1,1,1,1,1,1},
                        {1,3,3,3,1,1,1},
                        {1,1,2,0,1,1,1},
                        {1,1,0,0,1,1,1},
                        {1,0,2,2,0,1,1},
                        {1,0,0,0,1,1,1},
                        {1,0,0,1,1,1,1},
                        {1,1,1,1,1,1,1}};
                this.posx=3;
                this.posy=3;
                this.level=2;
                break;
            case 3:
                this.mapcode=new int[][]{{1,1,1,1,1,1,1},
                        {1,0,0,0,1,1,1},
                        {1,3,0,2,1,1,1},
                        {1,3,0,0,0,0,1},
                        {1,1,2,0,1,0,1},
                        {1,1,0,0,0,0,1},
                        {1,1,1,1,1,1,1}};
                this.posx=3;
                this.posy=3;
                this.level=3;
                break;
            case 4:
                this.mapcode=new int[][]{{1,1,1,1,1,1,1,1,1,1},
                        {1,1,1,3,3,3,3,1,1,1},
                        {1,1,1,1,3,3,1,1,1,1},
                        {1,1,1,1,2,2,1,1,1,1},
                        {1,1,1,1,0,0,1,1,1,1},
                        {1,1,0,0,2,2,2,0,0,1},
                        {1,0,0,0,0,0,0,0,0,1},
                        {1,0,2,0,1,1,1,1,1,1},
                        {1,0,0,0,1,1,1,1,1,1},
                        {1,1,1,1,1,1,1,1,1,1}};
                this.posx=4;
                this.posy=4;
                this.level=4;
                break;
            case 5:
                this.mapcode=new int[][]{   {0,1,1,1,1,1,0,1,1,1,1,1,1,0},
                        {0,1,0,0,0,1,1,1,0,0,0,0,1,0},
                        {1,1,0,2,0,2,0,1,2,0,1,2,1,0},
                        {1,0,0,2,0,0,0,2,0,0,2,0,1,1},
                        {1,0,1,0,0,1,1,0,1,3,3,3,3,1},
                        {1,0,0,1,1,0,2,0,1,3,1,1,3,1},
                        {1,1,0,0,2,0,0,0,0,3,3,3,3,1},
                        {0,1,0,2,2,0,1,2,1,3,3,3,3,1},
                        {0,1,0,0,0,1,0,0,0,1,2,0,1,1},
                        {0,1,1,1,1,1,0,2,0,0,0,0,1,0},
                        {0,0,0,0,0,1,1,1,1,0,0,1,1,0},
                        {0,0,0,0,0,0,0,0,1,1,1,1,1,0}};
                this.posx=5;
                this.posy=3;
                this.level=5;
                break;
            case 6:
                this.mapcode=new int[][]{   {0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1},
                        {0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
                        {0,0,0,0,0,0,0,1,2,0,2,2,2,0,0,1},
                        {0,0,0,0,0,0,0,1,0,0,0,1,0,2,0,1},
                        {0,0,0,0,0,0,0,1,0,0,0,2,0,2,0,1},
                        {0,0,0,0,0,1,1,1,2,0,2,0,1,0,1,1},
                        {0,0,0,0,0,1,0,0,0,2,1,2,1,0,1,0},
                        {1,1,1,1,1,1,0,1,0,0,1,0,0,0,1,0},
                        {1,3,3,3,0,0,0,1,0,0,1,0,1,1,1,0},
                        {1,3,3,3,3,3,3,0,0,0,1,0,1,0,0,0},
                        {1,3,3,3,3,3,3,1,0,2,1,0,1,0,0,0},
                        {1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,0},
                        {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0}};
                this.posx=12;
                this.posy=4;
                this.level=6;
                break;
            case 7:
                this.mapcode=new int[][]{{1,1,1,1,0,0,0,0,0,0,0,0,0,0,0},
                        {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
                        {1,0,0,1,1,1,1,1,1,1,1,1,1,0,0},
                        {1,0,0,0,0,1,1,0,0,0,0,0,1,0,0},
                        {1,3,3,1,0,0,0,0,2,2,1,0,1,0,0},
                        {1,3,3,0,0,1,1,0,0,0,2,0,1,1,1},
                        {1,3,3,1,0,0,1,1,2,1,0,2,0,0,1},
                        {1,3,3,0,0,0,1,0,0,2,0,2,0,0,1},
                        {1,3,3,1,0,0,1,0,2,0,2,0,0,0,1},
                        {1,0,3,0,0,0,1,0,2,0,2,0,1,1,1},
                        {1,0,0,1,0,0,1,0,1,0,1,1,1,0,0},
                        {1,0,0,1,0,0,0,0,1,1,1,1,1,0,0},
                        {1,1,1,1,1,1,1,1,1,0,0,0,0,0,0}};
                this.posx=4;
                this.posy=4;
                this.level=7;
                break;
            default:
                this.mapcode=new int[][]{{1,1,1,1,1,1,1},
                        {1,1,1,3,1,1,1},
                        {1,1,1,2,1,1,1},
                        {1,3,2,0,2,3,1},
                        {1,1,1,2,1,1,1},
                        {1,1,1,3,1,1,1},
                        {1,1,1,1,1,1,1}};
                this.posx=3;
                this.posy=3;
                this.level=1;
        }

        this.starty=7-(this.mapcode.length)/2;
        this.startx=10-(this.mapcode[0].length)/2;
    }
    public map(){
        this.mapcode=new int[][]{{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,3,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,2,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};
        this.starty=7-(this.mapcode.length)/2;
        this.startx=10-(this.mapcode[0].length)/2;
        this.posx=-10;
        this.posy=-10;
        this.level=0;
    }
    public void setmap(int x,int y){
        System.out.println("x="+x+"y="+y);
        int codex=x/50;
        int codey=y/50;
        this.mapcode [codey][codex] = (this.mapcode[codey][codex]+1)%4;
    }
    public void draw(Graphics g){
        for(int y=0;y<this.mapcode.length;y++){
            for(int x=0;x<this.mapcode[0].length;x++){
                switch (this.mapcode[y][x]) {
                    case 0:
                        break;
                    case 1:
                        g.setColor(Color.BLACK);
                        g.fillRect((this.startx+x)*50, (this.starty+y)*50, 50, 50);
                        break;

                    case 2:
                        g.setColor(Color.YELLOW);
                        g.fillRect((this.startx+x)*50, (this.starty+y)*50, 50, 50);
                        break;

                    case 3:
                        g.setColor(Color.RED);
                        g.fillRect((this.startx+x)*50, (this.starty+y)*50, 50, 50);
                        break;
                    case 4:
                        g.setColor(Color.GREEN);
                        g.fillRect((this.startx+x)*50, (this.starty+y)*50, 50, 50);
                        break;
                    default:
                        g.fillRect((this.startx+x)*50, (this.starty+y)*50, 50, 50);
                        break;
                }

            }
        }
        g.setColor(Color.BLUE);
        g.fillRect((this.startx + this.posx) * 50, (this.starty + this.posy) * 50, 50, 50);
        System.out.println("in draw x="+this.startx*50+"  y="+this.starty*50 +"  (by panel)");
    }

    public void move(int code){
        //System.out.println("an le "+code+"jian");
        //System.out.println("????????!!"+this.mapcode[3][3]);
        switch (code) {
            case KeyEvent.VK_UP:
                this.ifcanmove(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                this.ifcanmove(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                this.ifcanmove(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                this.ifcanmove(1, 0);
                break;
            default:
                break;
        }

        //System.out.println("????????"+this.mapcode[3][3]);
    }
    public boolean ifyouwin(){
        for(int y=0;y<this.mapcode.length;y++){
            for(int x=0;x<this.mapcode[0].length;x++){
                if(this.mapcode[y][x]==3){
                    return false;
                }
            }
        }
        return true;
    }
    private void ifcanmove(int x,int y){
        if(this.mapcode[this.posy+y][this.posx+x]==0||this.mapcode[this.posy+y][this.posx+x]==3){
            this.posy+=y;
            this.posx+=x;
            this.step+=1;
        }else if (this.mapcode[this.posy+y][this.posx+x]==2) {
            if(this.mapcode[this.posy+2*y][this.posx+2*x]==0){

                this.mapcode[this.posy+2*y][this.posx+2*x]=2;
                this.mapcode[this.posy+y][this.posx+x]=0;
                this.posy+=y;
                this.posx+=x;
                this.step+=1;
            }else if (this.mapcode[this.posy+2*y][this.posx+2*x]==3) {
                this.mapcode[this.posy+2*y][this.posx+2*x]=4;
                this.mapcode[this.posy+y][this.posx+x]=0;
                this.posy+=y;
                this.posx+=x;
                this.step+=1;
            }

        }else if (this.mapcode[this.posy+y][this.posx+x]==4) {
            if(this.mapcode[this.posy+2*y][this.posx+2*x]==0){
                this.mapcode[this.posy+2*y][this.posx+2*x]=2;
                this.mapcode[this.posy+y][this.posx+x]=3;
                this.posy+=y;
                this.posx+=x;
                this.step+=1;
            }else if (this.mapcode[this.posy+2*y][this.posx+2*x]==3) {
                this.mapcode[this.posy+2*y][this.posx+2*x]=4;
                this.mapcode[this.posy+y][this.posx+x]=3;
                this.posy+=y;
                this.posx+=x;
                this.step+=1;
            }
        }
    }
}