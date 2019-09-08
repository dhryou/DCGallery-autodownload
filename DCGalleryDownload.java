import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.net.*;
import java.util.*;

class DateTime{
	DateTimeFormatter formatter_shortdate = DateTimeFormatter.ofPattern("yyMMdd");
	DateTimeFormatter formatter_mediumdate = DateTimeFormatter.ofPattern("yyMMdd_HHmm");
	DateTimeFormatter formatter_longdate = DateTimeFormatter.ofPattern("LLL dd, yyyy (hh:mm:ss a)");
	LocalDateTime time = LocalDateTime.now(ZoneId.of("GMT+9")).withNano(0);
	String shortdate = time.format(formatter_shortdate);
	String mediumdate = time.format(formatter_mediumdate);
	String longdate = time.format(formatter_longdate);
	int rank = 0;

	DateTime(){
		time = LocalDateTime.now();
	}
	public String getShortDate(){
		return shortdate;
	}
	public String getMediumDate(){
		return mediumdate;
	}
	public String getLongDate(){
		return longdate;
	}
}

class Menu extends JFrame implements ActionListener{
	JPanel westpanel, eastpanel;
		JPanel buffer;
	JPanel contentpanel;
	JPanel northpanel;
		JLabel title, version;
	JPanel centerpanel;
		String latestdate_string, prevpostnum_string;
		JLabel latestdate_label, prevpostnum_label;
		JButton downloadBtn;
	JPanel southpanel;
		JLabel developer;
	int prevpostnum, latestpostnum;
	String txtpostdate, latestdate;

	Menu(String latestdate, String prevdownloaddate, int prevpostnum, String txtpostdate){
		setTitle("Seulgi Gallery");
		setSize(600,400);
		setBackground(new Color(253,231,86));
		setLocationRelativeTo(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		westpanel = new JPanel();
		eastpanel = new JPanel();
		northpanel = new JPanel(new GridLayout(3,1));
			title = new JLabel("Seulgi DC Gallery Download", SwingConstants.CENTER);
				title.setFont(new Font("Hi Melody", Font.PLAIN, 56));
			version = new JLabel("Download v 1.2", SwingConstants.CENTER);
				version.setFont(new Font("Hi Melody", Font.PLAIN, 28));
		centerpanel = new JPanel(new GridLayout(3,1));
			latestdate_string = "Last download: " + prevdownloaddate;
			prevpostnum_string = "Last post: " + prevpostnum;
				this.prevpostnum = prevpostnum;
			latestdate_label = new JLabel(latestdate_string, SwingConstants.LEFT);
			prevpostnum_label = new JLabel(prevpostnum_string, SwingConstants.LEFT);
			downloadBtn = new JButton("Download images");
		southpanel = new JPanel(new GridLayout(3,1));
			developer = new JLabel("Developed by: not your ordinary fan     ", SwingConstants.RIGHT);


		this.add("West", westpanel);
			westpanel.add(new JLabel("           ", SwingConstants.CENTER));
		this.add("East", eastpanel);
			eastpanel.add(new JLabel("           ", SwingConstants.CENTER));
		this.add("North", northpanel);
      northpanel.add(new JPanel()); northpanel.add(title); northpanel.add(version);
    this.add("Center", centerpanel);
      centerpanel.add(latestdate_label);
      centerpanel.add(prevpostnum_label);
      centerpanel.add(downloadBtn);
				downloadBtn.addActionListener(this);
    this.add("South", southpanel);
      southpanel.add(new JPanel()); southpanel.add(developer); southpanel.add(new JPanel());
		setVisible(true);
		this.txtpostdate = txtpostdate;
		this.latestdate = latestdate;
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource() == downloadBtn) {
			downloadBtn.setText("Complete!");
			System.out.println("Download!");

			String gomgallery_home = "https://gall.dcinside.com/board/lists/?id=seulgi";
			//REPLACE the URL of the gallery above (e.g. id=seulgi --> id=joy)
			ImageDownload seulgi = new ImageDownload(gomgallery_home, txtpostdate, latestdate, prevpostnum);
			//You may replace the name of this variable, and all its uses below
			boolean powerup = false;
			try{
				seulgi.findlatestpostnum();
				powerup = seulgi.createrecomlist();
				if (powerup){
					seulgi.accessrecompost();
					seulgi.savedownloadhistory();
				}
			}
			catch(Exception exc){}
		}
	}
}

class ImageDownload{
	ArrayList <Integer> recomlist = new ArrayList <Integer>();
	int prevpostnum, latestpostnum, imgdownloadcount;
	int start, end;
	String gomgallery_home, txtpostdate, latestdate;

	URL homeURL, postURL, imgURL;
	HttpURLConnection conn;
	URLConnection uconn;
	InputStream is;
	InputStreamReader isr;
	BufferedReader bfr;
	FileOutputStream fos;
	String readString, postURLString, postTitle, history;

	ImageDownload(String gomgallery_home, String txtpostdate, String latestdate, int prevpostnum){
		this.gomgallery_home = gomgallery_home;
		this.txtpostdate = txtpostdate;
		this.latestdate = latestdate;
		this.prevpostnum = prevpostnum;
	}

	public void findlatestpostnum() throws IOException{
		homeURL = new URL(gomgallery_home);
		conn = (HttpURLConnection) homeURL.openConnection();
		is = conn.getInputStream();
		isr = new InputStreamReader(is, "UTF-8");
		bfr = new BufferedReader(isr);

		readString = "";
		String datano = "";
		start = 0; end = 0; int i = 0;
		int [] home_indicesarr = new int[50];

		while ((readString = bfr.readLine()) != null){
			if (readString.indexOf("data-no") > 0){
				start = readString.indexOf("data-no") + 9;
				end = readString.indexOf(">") - 2;
				home_indicesarr[i++] = Integer.parseInt(readString.substring(start, end), 10);
			}
		}
		for(i=0;i<50;i++)
			if (home_indicesarr[i] > latestpostnum) latestpostnum = home_indicesarr[i];
		System.out.println("Latest post: " + latestpostnum);
		history = latestdate + "\nLatest post: " + latestpostnum + "\n\nPhotos:\n";

		bfr.close();
		isr.close();
		is.close();
	}
	public boolean createrecomlist() throws IOException{
		int recomcount = 0, start = 0, end = 0;
		int temppostnum = latestpostnum;

		for(; temppostnum > prevpostnum; temppostnum--){
			try{
				Thread t1 = Thread.currentThread();
				t1.sleep(553);
			}catch(Exception e){}

			postURLString = "https://gall.dcinside.com/board/view/?id=seulgi&no=" + temppostnum;
			//REPLACE the URL of the gallery above (e.g. id=seulgi --> id=joy)
			System.out.println("Checking post " + temppostnum);
			postURL = new URL(postURLString);
			conn = (HttpURLConnection) postURL.openConnection();
			try{is = conn.getInputStream();}
			catch(Exception exc){
				System.out.println("Deleted post: " + exc);
				continue;
			}
			isr = new InputStreamReader(is, "UTF-8");
			bfr = new BufferedReader(isr);

			readString = "";
			while ((readString = bfr.readLine()) != null){
				if (readString.indexOf("gall_reply_num") > 0){
					start = readString.indexOf("gall_reply_num") + 19;
					end = readString.indexOf("</span>");
					recomcount = Integer.parseInt(readString.substring(start, end), 10);
					if(recomcount >= 10)
						recomlist.add(temppostnum);
					break;
				}
			}

			bfr.close();
			isr.close();
			is.close();
			conn.disconnect();
		}

		for(int i = recomlist.size()-1; i > -1; i--)
			System.out.println("Recommended post: " + recomlist.get(i));
		System.out.println("-----------RECOMLIST SCAN COMPLETE-----------");

		if (recomlist.size() != 0) return true;
		else return false;
	}
	public void accessrecompost() throws IOException{
		start = 0; end = 0;
		postTitle = "";

		for(int i = recomlist.size()-1; i > -1; i--){
			try{
				Thread t1 = Thread.currentThread();
				t1.sleep(553);
			}catch(Exception e){}

			postURLString = "https://gall.dcinside.com/board/view/?id=seulgi&no=" + recomlist.get(i);
			//REPLACE the URL of the gallery above (e.g. id=seulgi --> id=joy)
			System.out.println(postURLString);
			postURL = new URL(postURLString);
			conn = (HttpURLConnection) postURL.openConnection();
			is = conn.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			bfr = new BufferedReader(isr);

			readString = "";
			while ((readString = bfr.readLine()) != null){
				if (readString.indexOf("<title>") >= 0){
					end = readString.indexOf(" - 슬기 갤러리</title>");
					//REPLACE the name of the gallery above (e.g. "슬기" --> "조이")
					postTitle = readString.substring(8,end);
					System.out.println(postTitle);
				}
				if (readString.indexOf("Dc_App_Img") >= 0){
					String imgarr[] = readString.split("img src=");
					for(int j=1; j<imgarr.length; j++){
						imgarr[j] = imgarr[j].substring(1);
						end = imgarr[j].indexOf("\"");
						imgarr[j] = imgarr[j].substring(0, end);
						try{downloadimage(postTitle, recomlist.get(i), imgarr[j]);}
						catch(Exception exc){System.out.println(exc);}
					}
				}
			}
			bfr.close();
			isr.close();
			is.close();
			conn.disconnect();
		}
	}
	public void downloadimage(String postTitle, int postnum, String imageURL) throws Exception{
		String imgtitle = "";
		int count = 1, imgsize = 0;
		File imgfile;
		byte[] bytearr;

	  imgURL = new URL(imageURL);
	  uconn = imgURL.openConnection();
	  uconn.connect();
	  imgsize = uconn.getContentLength();
	  System.out.println("File size : "+(imgsize/1000)+"KB");

		do{
			imgtitle = postTitle + " " + count++ + ".jpg";
			imgfile = new File("/Users/(Mac Computer Username)/Desktop/" + imgtitle);
			//Save to a folder of your choice anywhere, as long as directory is correct
		}
		while(imgfile.exists());
		System.out.println(imgtitle);
	  fos = new FileOutputStream(imgfile);
	  is = uconn.getInputStream();
	  bytearr = new byte[imgsize];

	  for(int i=0; i<imgsize; i++)
	    bytearr[i] = (byte)is.read();
		fos.write( bytearr );
	  fos.close();
		is.close();

		imgtitle = String.format("%-50s", imgtitle);
		history += imgtitle + "\t(" + (imgsize/1000) + "KB" + ", " + postnum + ")" + "\n";
		imgdownloadcount++;
	}
	public void savedownloadhistory() throws Exception{
		FileWriter fw;
		String latestpoststr = latestdate + "\r\n" + recomlist.get(0);
		history += "\nTOTAL IMAGES DOWNLOADED: " + imgdownloadcount;

		File prevpostfile = new File("/Users/(Mac Computer Username)/Desktop/temp.txt");
		fw = new FileWriter(prevpostfile);
		fw.write(latestpoststr);
		fw.close();
		File txtfile = new File("/Users/(Mac Computer Username)/Desktop/" + txtpostdate + ".txt");
		fw = new FileWriter(txtfile);
		fw.write(history);
		fw.close();

		System.out.println("--------------DOWNLOAD COMPLETE--------------");
	}
}

public class DCGalleryDownload
{
	public static void main(String[] args) throws IOException, Exception
	{
		int prevpostnum = 0;
		DateTime datetime = new DateTime();
		String latestdate = datetime.getLongDate();
		String txtpostdate = datetime.getMediumDate();
		String prevdownloaddate = "";
		try{
			File file = new File("/Users/(Mac Computer Username)/Desktop/temp.txt");
			FileReader filereader = new FileReader(file);
			BufferedReader buffreader = new BufferedReader(filereader);
			String line = "";
			if((line=buffreader.readLine()) != null){
				prevdownloaddate = line;
				line = buffreader.readLine();
				prevpostnum = Integer.parseInt(line);
				System.out.println("Latest download: " + prevdownloaddate + ", " + prevpostnum);
			}
			buffreader.close();
			filereader.close();
		}
		catch(Exception ex){System.out.println(ex);}

		Menu menu = new Menu(latestdate, prevdownloaddate, prevpostnum, txtpostdate);
	}
}
