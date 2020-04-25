package com.lzl.frame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Myjpan extends JPanel {
	//初始方格长度
	volatile int buchang = 10;
	
	volatile boolean b = false;
	//记录窗口大小变化后上一次的窗口大小，窗体大小变化后重新排列
	volatile int w1;
	volatile int h1;
	//储存活细胞坐标的集合
	volatile List<ZuoBiao> list = new ArrayList<ZuoBiao>();
	//储存死亡的活细胞集合
	volatile List<ZuoBiao> list2 = new ArrayList<ZuoBiao>();
	//rule坐标信息读取后先存放在这里
	volatile List<ZuoBiao> list3 = new ArrayList<ZuoBiao>();
	//左右下内边距
	int xx = 0;
	//上边距
	int yy = 0;
	//数组大小与方格数相同  记录细胞状态  活为1 死为0
	volatile int[][] a;
	//每过多少毫秒演化一次
	volatile int sudu = 1000;
	volatile boolean r1 = false;
	volatile String rule = "";

	
	
	JButton jb = new JButton();
	JButton jb3 = new JButton("加快");
	JButton jb2 = new JButton("自动");
	JButton jb4 = new JButton("随机");
	JButton jb5 = new JButton("初始化");
	JButton jb6 = new JButton("导入");
	JFrame jFrame = new JFrame();
	//下拉框
	JComboBox<String> jc = new JComboBox<>(new String[] { "无", "星门", "震荡循环集合", "高斯帕滑翔机" });

	public Myjpan() {
		
		jFrame.setBounds(300, 300, 600, 520);
		jFrame.add(this);
		
		w1 = jFrame.getWidth();
		h1 = jFrame.getHeight();
		//设置窗口关闭
		jFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			};

		});
		jFrame.setVisible(true);
		jFrame.setLayout(null);
		//添加窗口监听
		jFrame.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
			//窗口发生该变时调用
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				//判断窗口变大变小
				if (w1 < e.getComponent().getWidth() || h1 < e.getComponent().getHeight()) {
					//计算出变化后的网格长宽
					int whi = e.getComponent().getWidth() - xx * 2;
					int hei = (e.getComponent().getHeight() - xx - yy);
					//计算变化后的网格数
					whi = (whi - whi % buchang) / buchang;
					hei = (hei - hei % buchang) / buchang;
					//简历对应新窗体大小的数组
					int[][] b = new int[whi][hei];
					//初始化数组
					initArray(b);
					
					for (int i = 0; i < list.size(); i++) {
						//将活细胞坐标设置为适应新窗体大小的坐标，并设置该位置的状态
						ZuoBiao z = list.get(i);
						z.setX(z.getX() + whi / 2 - a.length);
						z.setY(z.getY() + hei / 2 - a[0].length);
						b[z.getX()][z.getY()] = 1;
					}
					//将a指向设置好的B数组
					a = b;
					//重设上次窗体大小
					w1 = e.getComponent().getWidth();
					h1 = e.getComponent().getHeight();
					//页面重画
					repain();
				}
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});
		//添加鼠标点击事件
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				//获取鼠标点击位置的坐标对象
				ZuoBiao zuoBiao = zhuanghuan(e.getX(), e.getY());
				//如果原来该位置的状态为0则点击为创建活细胞  否则为消除活细胞
				if (a[zuoBiao.getX()][zuoBiao.getY()] != 1) {
					a[zuoBiao.getX()][zuoBiao.getY()] = 1;
					
					list.add(zuoBiao);
				} else {
					a[zuoBiao.getX()][zuoBiao.getY()] = 0;
					list2.add(zuoBiao);
				}
				repain();
			}
		});
		
		jb.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				//演化一次
				start();
				repain();
				b = false;
			}
		});
		Container container = jFrame.getContentPane();
		container.setLayout(null);
		jb.setText("逐步");
		jb.setBounds(0, 0, 80, 30);
		container.add(jb);

		jb2.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				//创建线程自动演化
				if(!b){
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							b = true;
							while (b) {
								try {
									Thread.sleep(sudu);
								} catch (Exception e2) {
									// TODO: handle exception
								}
								start();
								repain();
							}
							
						}
					}).start();
				}
				
			}
		});
		jb2.setBounds(82, 0, 80, 30);
		container.add(jb2);
		jFrame.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				//通过滚轮调节网格大小
				// TODO Auto-generated method stub
				if (a.length > 30) {
					//缩小
					if (e.getWheelRotation() < 0) {
						buchang = buchang + 1;
						int whi = e.getComponent().getWidth() - xx * 2;
						int hei = (e.getComponent().getHeight() - xx - yy);
						whi = (whi - whi % buchang) / buchang;
						hei = (hei - hei % buchang) / buchang;
						int[][] b = new int[whi][hei];
						initArray(b);
						for (int i = 0; i < list.size(); i++) {
							ZuoBiao z = list.get(i);
							if (z.getX() - 1 > 0 && z.getY() - 1 > 0 && z.getX() + 1 < a.length
									&& z.getY() + 1 < a[0].length) {
								z.setX(z.getX() - 1);
								z.setY(z.getY() - 1);
								b[z.getX()][z.getY()] = 1;
							} else {
								list.remove(z);
							}
						}
						a = b;
						repain();
					} else {//放大
						if (e.getWheelRotation() > 0) {
							buchang = buchang - 1;
							int whi = e.getComponent().getWidth() - xx * 2;
							int hei = (e.getComponent().getHeight() - xx - yy);
							whi = (whi - whi % buchang) / buchang;
							hei = (hei - hei % buchang) / buchang;
							int[][] b = new int[whi][hei];
							initArray(b);
							for (int i = 0; i < list.size(); i++) {
								ZuoBiao z = list.get(i);
								if (z.getX() - 1 > 0 && z.getY() - 1 > 0 && z.getX() + 1 < a.length
										&& z.getY() + 1 < a[0].length) {
									z.setX(z.getX() + 1);
									z.setY(z.getY() + 1);
									b[z.getX()][z.getY()] = 1;
								} else {
									list.remove(z);
								}
							}
							a = b;
							repain();
						}
					}
				} else {
					if (e.getWheelRotation() > 0) {
						buchang = buchang - 1;
						int whi = e.getComponent().getWidth() - xx * 2;
						int hei = (e.getComponent().getHeight() - xx - yy);
						whi = (whi - whi % buchang) / buchang;
						hei = (hei - hei % buchang) / buchang;
						int[][] b = new int[whi][hei];
						initArray(b);
						for (int i = 0; i < list.size(); i++) {
							ZuoBiao z = list.get(i);
							if (z.getX() - 1 > 0 && z.getY() - 1 > 0 && z.getX() + 1 < a.length
									&& z.getY() + 1 < a[0].length) {
								z.setX(z.getX() + 1);
								z.setY(z.getY() + 1);
								b[z.getX()][z.getY()] = 1;
							} else {
								list.remove(z);
							}
						}
						a = b;
						repain();
					}
				}
			}
		});

		jb3.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				//加快演化速度
				sudu *= 0.8;
			}
		});
		jb3.setBounds(164, 0, 80, 30);

		container.add(jb3);
		jb4.setBounds(244, 0, 80, 30);
		jb4.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				//随机产生细胞
				int x;
				int y;
				for (int i = 0; i < a.length * a[0].length * 0.3; i++) {
					do {
						x = (int) (Math.random() * a.length);
						y = (int) (Math.random() * a[0].length);
					} while (a[x][y] == 1);
					a[x][y] = 1;
					list.add(new ZuoBiao(x, y));
				}
				repain();

			}
		});
		container.add(jb4);
		jb5.setBounds(324, 0, 80, 30);
		jb5.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			//初始化
			public void mouseClicked(MouseEvent e) {
				
				String s = "";
				System.out.println((String)jc.getSelectedItem());
				if(rule.equals("")){
					switch ((String)jc.getSelectedItem()) {
					case "无":
						cle();
						break;
					case "星门":
						cle();
						rule=MyString.STR1;
						break;
					case "震荡循环集合":
						cle();
						rule=MyString.STR3;
						break;
					case "高斯帕滑翔机":
						cle();
						rule=MyString.STR2;
						break;
					}
				}
				if (!rule.equals("")) {
					s = rule;
					s = s.replace("2$", "$b$");
					String[] split = s.split("\\$");
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < split.length; i++) {
						if (split[i].length() >= 1) {
							sb.append(split[i]);
							System.out.println(split[i]);
							if (split[i].charAt(split[i].length() - 1) == 'b'
									|| split[i].charAt(split[i].length() - 1) == 'o') {
								sb.append("$");
								continue;
							} else {
								String string = split[i];
								int star = 0;
								int end = string.length();
								for (int q = string.length() - 1; q >= 0; q--) {
									if (string.charAt(q) == 'b' || string.charAt(q) == 'o') {
										star = q + 1;
										break;
									}
								}
								System.out.println(star);
								System.out.println(end);
								int cishu = Integer.parseInt(string.substring(star, end));
								for (int j = 0; j < cishu; j++) {
									if (j < cishu - 1) {
										sb.append("$b");
									} else {
										sb.append("$");
									}
								}
							}

						}
					}
					System.out.println("字符串处理完成");
					String[] split2 = sb.toString().split("\\$");
					int newwi = 0;
					int www = 0;
					int cha = 0;
					for (int i = 0; i < split2.length; i++) {

						newwi = initYuanZuobiao(split2[i].toCharArray(), i + cha);
						if (www < newwi) {
							www = newwi;
						}

					}
					int newhi = split.length;
					System.out.println(www);
					list3ToList1(www, newhi);
					
					repain();
					rule="";
				}
			}
		});
		container.add(jb5);
		jb6.setBounds(404, 0, 80, 30);
		jb6.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				JFrame jf = new JFrame();
				JButton jfjb = new JButton("完成");

				jfjb.setBounds(155, 150, 80, 30);
				jf.setBounds(200, 200, 400, 230);
				jf.setLayout(null);
				Container contentPane = jf.getContentPane();
				JTextArea textArea = new JTextArea();
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setBounds(0, 0, 390, 150);
				jfjb.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						rule = textArea.getText().replaceAll(" ", "").replace("\n", "");
						jf.dispose();
					}
				});
				contentPane.add(textArea);
				contentPane.add(jfjb);
				jf.setVisible(true);
			}
		});
		container.add(jb6);

		jc.setBounds(484, 0, 80, 30);
		container.add(jc);
	}
	public void cle(){
		list=new ArrayList<>();
		list2=new ArrayList<>();
		list3=new ArrayList<>();
		jFrame.setBounds(300, 300, 600, 520);
		w1 = jFrame.getWidth();
		h1 = jFrame.getHeight();
		int w = jFrame.getWidth() - 2 * xx;
		int h = jFrame.getHeight() - xx - yy;
		a = new int[w / buchang][h / buchang];
		buchang=10;
		initArray(a);
		rule="";
		repain();
		
	}
	public void list3ToList1(int w, int h) {
		// System.out.println(list3.size());
		if(w>a.length/2){
			for (int i = 1; a.length * i / 2 < w; i++) {
				buchang -= i * 3.1;
				a = new int[(int)(a.length+w)][(int)(a[0].length/1.5)+h+10];
			}
		}
		
		for (int i = 0; i < list3.size(); i++) {

			ZuoBiao z = list3.get(i);
			z.setX(z.getX() + a.length / 6);
			z.setY(z.getY() + a[0].length/10);
			list.add(z);
			// System.out.println(z.getX()+","+z.getY());
			a[z.getX()][z.getY()] = 1;

		}
		
		System.out.println(a[0].length);
		jFrame.setSize(w1 = a.length * buchang + xx, h1 = a[0].length * buchang + yy + xx);
	}

	private int initYuanZuobiao(char[] s, int i) {
		int sum = 0;
		int parseInt = 0;
		int mmm = 0;
		StringBuffer num = new StringBuffer();
		for (int j = 0; j < s.length; j++) {
			try {
				int parseInt1 = Integer.parseInt(s[j] + "");
				parseInt = parseInt1;
				num.append(parseInt1);

			} catch (Exception e) {
				if (s[j] == 'b') {		
					parseInt = 0;
				} else if (parseInt == 0) {
					addList3(1, s[j] + "", i, sum);
				} else {
					addList3(Integer.parseInt(num.toString()), s[j] + "", i, sum);
				}
				sum += Integer.parseInt(num.toString().equals("") ? "1" : num.toString());
				parseInt = 0;
				num.delete(0, num.length());
			}

		}

		return sum;
	}

	public void addList3(int i, String aorb, int y, int sum) {

		if (aorb.equals("o")) {
			for (int j = 0; j < i; j++) {
				list3.add(new ZuoBiao(sum + j, y));
			}
		}
	}

	public void repain() {
		// Graphics graphics = jFrame.getComponent(0).getGraphics();
		this.setBounds(0, 30, jFrame.getWidth(), jFrame.getHeight());
		this.repaint();
	}

	@Override
	public void update(Graphics g) {
		Image ImageBuffer = createImage(jFrame.getWidth(), jFrame.getHeight());
		Graphics GraImage = ImageBuffer.getGraphics();
		System.out.println("双缓存");
		paint(GraImage);
		GraImage.dispose();
		g.drawImage(ImageBuffer, 0, 0, jFrame);
	}

	public void paint(Graphics g) {

		super.paint(g);
		Graphics graphics = this.getGraphics();

		jb.repaint();
		jb2.repaint();
		jb3.repaint();
		jb4.repaint();
		jb5.repaint();
		jb6.repaint();
		jc.repaint();
		int w = this.getWidth() - 2 * xx;
		int h = this.getHeight() - xx - yy;
		if (a == null) {
			a = new int[w / buchang][h / buchang];
			initArray(a);
		}
		h = h - h % buchang;
		w = w - w % buchang;
		g.setColor(new Color(230, 230, 230));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.white);
		for (int i = 0; i < w / buchang + 1; i++) {
			g.drawLine(xx + i * buchang, yy, xx + i * buchang, h + yy);
		}
		for (int i = 0; i < h / buchang + 1; i++) {
			g.drawLine(xx, yy + i * buchang, w + xx, yy + i * buchang);
		}
		if (a == null) {
			a = new int[w / buchang][h / buchang];
			initArray(a);
		}
		g.setColor(Color.red);

		for (int i = 0; i < list.size(); i++) {
			//System.out.println(list2.size()+"---------------");
			if (list2.size() == 1) {
				if (list2.get(0).getX() == list.get(i).getX() && list2.get(0).getY() == list.get(i).getY()) {
					g.setColor(new Color(230, 230, 230));
					int n = list.get(i).getX() * buchang + xx;
					int m = list.get(i).getY() * buchang + yy;
					g.fillRect(n + 1 + 1, m + 1 + 1, buchang - 2, buchang - 2);
					g.setColor(Color.red);
					list.remove(i);
					list2.clear();
					continue;
				}
			}
			int n = list.get(i).getX() * buchang + xx;
			int m = list.get(i).getY() * buchang + yy;
			g.fillRect(n + 1 + 1, m + 1 + 1, buchang - 3, buchang - 3);
			
		}
		// g.fillRect(100, 100, 100, 100);
		System.out.println(list.size());
		list2.clear();
		g.dispose();
	}
	//将点击的坐标转化为对应的方格坐标
	public ZuoBiao zhuanghuan(int x, int y) {
		x = (x - xx) / buchang;
		y = (y - yy) / buchang;
		return new ZuoBiao(x, y);
	}

	public void initArray(int[][] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				a[i][j] = 0;
			}
		}
	}
	//计算演化一次后的细胞分布
	public void start() {
		//先将演化后的活细胞坐标存在这里
		ArrayList<ZuoBiao> arrayList = new ArrayList<>();
		list2.clear();
		//用来记录演化后的状态  防止创建过多相同坐标活细胞 
		int[][] p = new int[a.length][a[0].length];
		//用来记录哪个点被判断过
		int[][] k = new int[a.length][a[0].length];
		initArray(p);
		initArray(k);	
		for (ZuoBiao z : list) {	
			isnull(z, arrayList, p, k);	
		}
		list = arrayList;
		//初始化a
		initArray(a);
		//遍历活细胞
		for (ZuoBiao z : arrayList) {
			a[z.getX()][z.getY()] = 1;
		}
		//遍历死细胞
		for (ZuoBiao z : list2) {
			a[z.getX()][z.getY()] = 0;
		}
	}
	
	//越界或者判断过返回false
	public boolean isnrno(int x, int y, int[][] k) {
		if (x >= 0 && y >= 0 && x <= a.length-1 && y <a[0].length &&k[x][y]!=1) {
			return true;
		}
		return false;
	}

	private void isnull(ZuoBiao z, ArrayList<ZuoBiao> arrayList, int[][] p, int[][] k) {
		// TODO Auto-generated method stub
		
		if (isnrno(z.getX(), z.getY(), k)) {
			k[z.getX()][z.getY()] = 1;
			startgo(z.getX(), z.getY(), arrayList, p);
		}
		if (isnrno(z.getX() - 1, z.getY() - 1, k)) {
			k[z.getX() - 1][z.getY() - 1] = 1;
			startgo(z.getX() - 1, z.getY() - 1, arrayList, p);
		}
		if (isnrno(z.getX(), z.getY() - 1, k)) {
			k[z.getX()][z.getY() - 1] = 1;
			startgo(z.getX(), z.getY() - 1, arrayList, p);
		}
		if (isnrno(z.getX() + 1, z.getY() - 1, k)) {
			k[z.getX() + 1][z.getY() - 1] = 1;
			startgo(z.getX() + 1, z.getY() - 1, arrayList, p);
		}
		if (isnrno(z.getX() - 1, z.getY(), k)) {
			k[z.getX() - 1][z.getY()] = 1;
			startgo(z.getX() - 1, z.getY(), arrayList, p);
		}
		if (isnrno(z.getX() + 1, z.getY(), k)) {
			k[z.getX() + 1][z.getY()] = 1;
			startgo(z.getX() + 1, z.getY(), arrayList, p);
		}
		if (isnrno(z.getX() - 1, z.getY() + 1, k)) {
			k[z.getX() - 1][z.getY() + 1] = 1;
			startgo(z.getX() - 1, z.getY() + 1, arrayList, p);
		}
		if (isnrno(z.getX(), z.getY() + 1, k)) {
			k[z.getX()][z.getY() + 1] = 1;
			startgo(z.getX(), z.getY() + 1, arrayList, p);
		}
		if (isnrno(z.getX() + 1, z.getY() + 1, k)) {
			k[z.getX() + 1][z.getY() + 1] = 1;
			startgo(z.getX() + 1, z.getY() + 1, arrayList, p);
		}
		
	}

	private void startgo(int x, int y, ArrayList<ZuoBiao> arrayList, int[][] p) {
		// TODO Auto-generated method stub
		//int w=0;
		//int z=0;
		if (zhouwei(x, y) == 1) {
			if (p[x][y] != 1) {
				p[x][y] = 1;
				arrayList.add(new ZuoBiao(x, y));
			}
		} else if (p[x][y] != 0) {
				p[x][y]=0;
				//System.out.println("z"+z++);
				list2.add(new ZuoBiao(x, y));
		}
		
	}
	//判断传过来的细胞下次状态  活为1 死为0
	private int zhouwei(int x, int y) {
		// TODO Auto-generated method stub
		int cou = 0;
		try {
			if (a[x - 1][y - 1] == 1) {
				cou++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (a[x][y - 1] == 1) {
				cou++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (a[x + 1][y - 1] == 1) {
				cou++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (a[x - 1][y] == 1) {
				cou++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (a[x + 1][y] == 1) {
				cou++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (a[x - 1][y + 1] == 1) {
				cou++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (a[x][y + 1] == 1) {
				cou++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (a[x + 1][y + 1] == 1) {
				cou++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		int result = 0;
		if (a[x][y] == 1 && cou == 2) {
			result = 1;
		}
		if (cou == 3) {
			result = 1;
		}
		if (cou > 3) {
			result = 0;
		}
		return result;
	}
}
