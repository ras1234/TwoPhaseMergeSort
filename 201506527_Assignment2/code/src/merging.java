/**
 * Running Time: Fixed main memory size vary input file size
 * 5MB, 100MB RAM :- 10 seconds
 * 50MB, 100MB RAM:- 40 seconds 
 * 500MB, 100MB RAM :- 1077 second  ie. 17 minutes and 95 seconds
 * 1GB, 100MB RAM :-1858 seconds 30 minutes 96 seconds
 * 2 GB 100MB RAM:- 56 minutes 43 seconds
 * 
 * 
 * Running Time:Fixed input file size and vary main memory size
 * 512 MB 10 MB RAM:-862 seconds
 * 512 MB 25 MB RAM:-1149 seconds
 * 512MB 100MB RAM:- 1113 seconds
 * 512MB 250MB 1170 seconds
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class SortingComparator implements Comparator<BinaryFileBuffer>
{
	@Override
	public int compare(BinaryFileBuffer bfilebuf1,BinaryFileBuffer bfilebuf2)
	{
		String str1=bfilebuf1.peek();
		String str2=bfilebuf2.peek();
		int sortingindex=0;
		for(int myit=0;myit<merging.getsortedcolumnlist.size();myit++)
		{
			sortingindex= merging.getindexofsortcolumn(merging.getcolumnlist.get(myit));
			if(!str1.split(",")[sortingindex].equals(str2.split(",")[sortingindex]))
				break;
		}
		if(merging.datatypeslist.get(sortingindex).contains("int"))
		{
			if(Integer.parseInt(str1.split(",")[sortingindex])< Integer.parseInt(str2.split(",")[sortingindex]))
			{
				if(merging.sortedorder.equals("desc"))
					return 1;
				else return -1;
			}
				//return (merging.sortedorder.equals("desc")?1:-1);
			else if(Integer.parseInt(str1.split(",")[sortingindex])== Integer.parseInt(str2.split(",")[sortingindex]))
				return 0;
			else 
				return (merging.sortedorder.equals("desc")?-1:1);
		}
		else if(merging.datatypeslist.get(sortingindex).contains("char"))
		{
			return str1.split(",")[sortingindex].compareToIgnoreCase(str2.split(",")[sortingindex]);
		}
		else if(merging.datatypeslist.get(sortingindex).contains("date"))
		{
			DateFormat datefrmt=new SimpleDateFormat("dd/MM/yyyy");
			try{
				int flag=datefrmt.parse(str1.split(",")[sortingindex]).compareTo(datefrmt.parse(str2.split(",")[sortingindex]));
				if(flag==-1)
				{
					if(merging.sortedorder.equals("desc"))
						return 1;
					else 
						return -1;
				}
					//return (merging.sortedorder.equals("desc")?1:-1);
				else if(flag==1)
				{
					if(merging.sortedorder.equals("desc"))
						return -1;
					else 
						return 1;
				}
					//return (merging.sortedorder.equals("desc")?-1:1);
				else 
					return 0;
		}catch(ParseException e){
			e.printStackTrace();
		}
	}
	return 0;
}
};

class OrderByComparator implements Comparator<String>
{
	@Override
	public int compare(String str1,String str2)
	{
		//System.out.println(str1);
		//System.out.println(str2);
		int sortingindex=0;
		//System.out.println("*"+merging.getsortedcolumnlist.size());
		for(int i=0;i<merging.getsortedcolumnlist.size();i++)
		{
			sortingindex=merging.getindexofsortcolumn(merging.getsortedcolumnlist.get(i));
			//System.out.println(sortingindex);
			if(!str1.split(",")[sortingindex].equals(str2.split(",")[sortingindex]))
			{
				break;
			}
		}
		if(merging.datatypeslist.get(sortingindex).contains("int"))
		{
			if(Integer.parseInt(str1.split(",")[sortingindex])< Integer.parseInt(str2.split(",")[sortingindex]))
			{
				if(merging.sortedorder.equals("desc"))
					return 1;
				else
					return -1;
			}
				//return(merging.sortedorder.equals("desc")?1:-1);
			else if(Integer.parseInt(str1.split(",")[sortingindex])==Integer.parseInt(str2.split(",")[sortingindex]))
				return 0;
			else
				return(merging.sortedorder.equals("desc")?-1:1);
		}
		else if(merging.datatypeslist.get(sortingindex).contains("char"))
		{
			return str1.split(",")[sortingindex].compareToIgnoreCase(str2.split(",")[sortingindex]);
		}
		else if(merging.datatypeslist.get(sortingindex).contains("date"))
		{
			DateFormat datefrmt=new  SimpleDateFormat("dd/MM/yyyy");
			try{
				int flag=datefrmt.parse(str1.split(",")[sortingindex]).compareTo(datefrmt.parse(str2.split(",")[sortingindex]));
				if(flag==-1)
				{
					if(merging.sortedorder.equals("desc"))
						return 1;
					else
						return -1;
				}
					//return (merging.sortedorder.equals("desc")?1:-1);
				else if(flag==1)
				{
					if(merging.sortedorder.equals("desc"))
						return -1;
					else
						return 1;
				}
					//return (merging.sortedorder.equals("desc")?-1:1);
				else 
					return 0;
			}catch(ParseException e){
				e.printStackTrace();
			}
		}
		return 0;
	}

};

class BinaryFileBuffer{
	public static int sizeofbuffer = 2048;
	public BufferedReader bufreadfile;
	public File Actualfilename;
	private String cachememory;
	private boolean emptyfile;

	public BinaryFileBuffer(File filename) throws IOException 
	{
		Actualfilename = filename;
		bufreadfile = new BufferedReader(new FileReader(filename), sizeofbuffer);
		functionreloading();
	}
	public boolean empty() 
	{
		return emptyfile;
	}

	private void functionreloading() throws IOException
	{
		try {
			if((this.cachememory = bufreadfile.readLine()) == null){
				emptyfile = true;
				cachememory = null;
			}else{
				emptyfile = false;
			}
		} catch(EOFException oef) {
			emptyfile = true;
			cachememory = null;
		}
	}

	public String peek() 
	{
		if(empty()) return null;
			return cachememory.toString();
	}
	
	public void close() throws IOException {
		bufreadfile.close();
	}

	public String pop() throws IOException
	{
		String answer = peek();
		functionreloading();
		return answer;
	}
}

public class merging {
	public static String metadatafile,inputfile,outputfile,sortedorder;
	public static Vector<String> getcolumnlist;
	public static Vector <String> getsortedcolumnlist;
	public static Vector <String> columnnamelist;
	public static Vector <String> datatypeslist;
	public static long mainMemorySize;
	public static long Recordsize = 0;
	public static long blocksize = 0;
	
	public static void main(String[] args) 
	{
		long Timebegins = System.currentTimeMillis();
		//parse the arguments and store into corresponding values
		ParseArguments(args);
		File filedescripter=new File(metadatafile);
		BufferedReader fileread=null;
		try{
			fileread=new BufferedReader(new FileReader(filedescripter));
		}catch(FileNotFoundException e){
			System.out.println("Unable to read metadata file " + metadatafile );
			System.exit(0);
		}
		int linecounter = 0;
		String line = null;
		columnnamelist = new Vector <String>();
		datatypeslist = new Vector <String>();
		try {
			while((line = fileread.readLine()) != null) {
				String str[] = line.split(",");
				try{
					columnnamelist.add(linecounter, str[0].toString().trim());
					datatypeslist.add(linecounter, str[1]);
					if(str[1].trim().contains("char")) 
					{
						str[1] = str[1].substring(str[1].indexOf("char(") + 5 , str[1].length() - 2);
						Recordsize += Long.parseLong(str[1]);
					}
					else if(str[1].trim().contains("date")) 
					{
						Recordsize += 10; 
					}
					else if(str[1].trim().contains("int")) 
					{
						Recordsize += 6;
					}
					else 
					{
						System.out.println("Format of  metadata file is wrong");
						System.exit(0);
						break;	
					}
					linecounter++;
					Recordsize += 1; //for the delimiter comma
				}catch(Exception e) {
					System.out.println("Wrong formatted metadata file");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			System.out.println("Unable to read metadata file " + metadatafile);
		}
		Recordsize-=1; // lastline taking one extra comma
		Recordsize+=1; // new line character at the end of file
		blocksize = (mainMemorySize*1024*1024)/Recordsize;
		System.out.println("RecordSize : " + Recordsize +"B");
		System.out.println("Block size : " + blocksize);
		
		//Two phase MergeSort 
		long TotalRecords=0;
		int countline=0;
		int blockcount=0;
		BufferedReader bufread=null;
		String strline=null;
		File filedes=null;
		ArrayList<String> blocklist=null;
		List<File> filelist=null;
		filedes=new File(inputfile);
		System.out.println(inputfile+":"+filedes.length());
		TotalRecords=filedes.length()/Recordsize;
		System.out.println("Number of records in file :" + TotalRecords + "(on average)");
		if(TotalRecords/blocksize > blocksize-1) 
		{
			System.err.println("Input file is large. Can cause 3 phase merge sort\nExiting...");
			System.exit(0);
		}
		long totalsublist=TotalRecords/blocksize;
		System.out.println("Total number of sublist files to be generated : " +totalsublist);
		try{
			bufread=new BufferedReader(new FileReader(new File(inputfile)));
			blocklist=new ArrayList<>();
			filelist=new ArrayList<>();
			while((strline=bufread.readLine())!=null)
			{
				if(countline==blocksize-1)
				{
					blockcount++;
					filelist.add(new File(createnewFile(blocklist,blockcount)));
					countline=0;
					blocklist.clear();
				}
				blocklist.add(strline);
				countline++;
			}
			if(countline < blocksize-1) 
			{
				blockcount++;
				filelist.add(new File(createnewFile(blocklist,blockcount)));
				countline=0;
			}	
		}catch(IOException e){
			System.out.println("Input File"+inputfile +"is not Found");
			System.exit(0);
		}
		//printing file data
		System.out.println("File list is:");
		for(int i=0;i<blockcount;i++)
		{
			System.out.println(filelist.get(i));
		}		
		try{
			System.out.println(sortedFilesaftermerging(filelist,new File(outputfile)));
		}catch(IOException e){
			e.printStackTrace();
		}
		long Timeends = System.currentTimeMillis();
		long elapsedtime=Timeends-Timebegins;
		System.out.println("Execution Time : " + elapsedtime/1000 +" sec.");
	}
	
	public static int sortedFilesaftermerging(List<File> subfiles,File outputfile) throws IOException
	{
		PriorityQueue<BinaryFileBuffer> buf=new PriorityQueue<BinaryFileBuffer> ((int)blocksize -1,new SortingComparator());		
		for (File f : subfiles) 
		{
			buf.add(new BinaryFileBuffer(f));
		}
		BufferedWriter fbw = new BufferedWriter(new FileWriter(outputfile));
		int rowcounter = 0;
		try {
			while(buf.size() > 0)
			{
				BinaryFileBuffer bfb = buf.poll();
				String r = bfb.pop();
				fbw.write(r);
				fbw.newLine();
				++rowcounter;
				if(bfb.empty()) {
					bfb.bufreadfile.close();
					bfb.Actualfilename.delete();// we don't need you anymore
				} else {
					buf.add(bfb); // add it back
				}
			}
		} catch(Exception e) {
			//System.out.println(e.printStackTrace());
		} finally { 
			fbw.close();
			for(BinaryFileBuffer bfb : buf ) 
				bfb.close();
		}
		return rowcounter;
	}	

	public static String createnewFile(ArrayList<String> blocklist,int filecounter)
	{
		String nameoffile="i-".concat(String.valueOf(filecounter));
		Collections.sort(blocklist,new OrderByComparator());	
		FileWriter filewriter = null;
		//create a sublist and corresponding files
		try {
			filewriter = new FileWriter(nameoffile);
			for(int i = 0; i < blocklist.size(); i++)
			{
				filewriter.write(blocklist.get(i)+"\n");
			}
			filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return nameoffile;
	}
	
	public static int getindexofsortcolumn(String colName)
	{

		int index = 0;
		for(index = 0; index <columnnamelist.size(); index++) 
		{
			if(columnnamelist.get(index).equals(colName))
			{
				return index;
			}
		}
		return -1;
	}
	
	private static void ParseArguments(String[] args) 
	{
		int argumentlength=args.length;
		if(argumentlength!=14)
		{
			System.out.println("Invalid Arguments");
			System.err.println("Usage : /n\"Init --meta_file <fileName> --input_file <fileName> ­--output_file <fileName> "+"­--output_column <col1,col2,...> --sort_column <col1,col2,...> --mm <size in MB> --order <asc/desc>\"");
			System.exit(0);
		}
		String heapsize;
		for(int i=0;i<argumentlength;i++)
		{
			switch(args[i].trim())
			{
//				case "-Xms256m":
//				heapsize=args[++i];
//				System.out.println(i);
//				break;
//				case "Xmx2096m":
//				heapsize=args[++i];
//				break;
				case "--meta_file":
					metadatafile=args[++i].trim();
					break;
				case "--input_file":
					inputfile=args[++i].trim();
					break;
				case "--output_file":
					outputfile=args[++i].trim();
					break;
				case "--output_column":
					String columnlistsplit[]=args[++i].trim().split(",");
					getcolumnlist=new Vector <String>();
					for(int j=0;j<columnlistsplit.length;j++)
					{
						getcolumnlist.add(columnlistsplit[j]);
					}
					break;
				case "--sort_column":
					String sortcolumnlistsplit[]=args[++i].trim().split(",");
					getsortedcolumnlist=new Vector<String>();
					for(int j=0;j<sortcolumnlistsplit.length;j++)
					{
						getsortedcolumnlist.add(sortcolumnlistsplit[j]);
					}
					break;
				case "--mm":
					mainMemorySize = Integer.parseInt(args[++i].trim());
					break;
				case "--order":
					sortedorder = args[++i];
					break;
				
				default:
					System.out.println("Error while taking arguments");
					System.exit(0);
					break;
			}
		}
		System.out.println("metaFileName : " + metadatafile );
		System.out.println("inputFile : " + inputfile );
		System.out.println("outputFile : " + outputfile );
		System.out.println("outputColumnList : " + getcolumnlist.toString());
		System.out.println("sortColumnList : " + getsortedcolumnlist.toString());
		System.out.println("mainMemorySize : " + mainMemorySize );
		System.out.println("sortOrder : " + sortedorder );
}
	
}
