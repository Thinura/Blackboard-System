package main.student.coursepanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;

import main.professor.professorframe.ProfCourseMaterialPanel;
import main.util.assignmentutils.assignment.Assignment;
import main.util.assignmentutils.assignmenttablemodel.AssignmentTableModel;
import main.util.download.Download;
import main.util.filechooser.FileChooser;
import main.util.filedetails.FileDetails;

public class AssignmentPanel extends JPanel {

    /**
     * Create the panel.
     */
    private List<Assignment> list;
    JTable table;
    private AssignmentTableModel atm;
    private String p;
    private AssignmentPanel cp=this;
    public CourseAssignmentPanel()
    {


    }

    /*Assume I have a path in my argument*/
    public AssignmentPanel(String path)throws Exception {
        this.setPreferredSize(new Dimension(1300,600));
        //this.setBorder(BorderFactory.createLineBorder(Color.RED));
        path+="assignments/";
        p=path;
        System.out.println("I have entered");
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 1320, 38);
        add(panel);
        panel.setLayout(null);

        JLabel lblAssignmentsUploaded = new JLabel("Assignments Uploaded :");
        lblAssignmentsUploaded.setBounds(0, 11, 184, 14);
        panel.add(lblAssignmentsUploaded);
        table=new JTable();

        list = new ArrayList<Assignment>();
		/*
		String str[] = FileDetails.getFileList(path);
		for(int i=0;i<str.length;i++)
		{
			Assignment temp = new Assignment();
			temp.setName("\""+str[i]+"\"");
			temp.setPath(path);
			String s[]=FileDetails.getStats(path,temp.getName() );
			temp.setLastModified(s[1]);
			temp.total_size=Double.parseDouble(s[0].substring(0, s[0].length()-3));
			temp.setSize(s[0]);
			list.add(temp);
		}
		*/
        atm = new AssignmentTableModel(list);
        System.out.println("########   "+list);
        table.setModel(atm);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(10, 510, 1320, 42);
        add(panel_2);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new Thread(){

                    public void run(){
                        try{
                            String str[] = FileDetails.getFileList(p);
                            list = new ArrayList<Assignment>();

                            JOptionPane optionPane = new JOptionPane("Please Wait...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

                            JDialog dialog = new JDialog();
                            dialog.setTitle("Message");
                            dialog.setModal(true);
                            dialog.setContentPane(optionPane);
                            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                            dialog.pack();
                            //System.out.println("what about here:::::::");
                            dialog.setBounds(500, 300, 350, 150);;

                            new Thread(){
                                public void run()
                                {try{
                                    for(int i=0;i<str.length;i++)
                                    {
                                        Assignment temp = new Assignment();
                                        temp.setName(str[i]);
                                        temp.setPath(p);
                                        String s[]=FileDetails.getStats(p,temp.getName() );
                                        temp.setLastModified(s[1]);
                                        temp.setSize(s[0]);
                                        list.add(temp);
                                        atm = new AssignmentTableModel(list);
                                        table.setModel(atm);
                                    }
                                    //System.out.println("I am here::::::");

                                    dialog.dispose();
                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                                }
                            }.start();
                            dialog.setVisible(true);
                            //System.out.println("I am hereeeeee:::::");

                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }}.start();
            }
        });

        JButton downloadbtn = new JButton("Download");
        downloadbtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                FileChooser fc = new FileChooser();
                String path = fc.getDirectoryPath();
                if(table.getSelectedRowCount()>0 && !path.equals("")){
                    int[] row = table.getSelectedRows();
                    new Thread(){

                        public void run(){
                            for(int r : row){
                                Assignment a = list.get(r);
                                String file = a.getPath()+a.getName();
                                Download dwn = new Download();
                                dwn.downloadFile(file, path,a);
                            }
                            JOptionPane.showMessageDialog(CourseAssignmentPanel.this,"Download Complete!!!!");
                        }
                    }.start();

                }else{
                    JOptionPane.showMessageDialog(CourseAssignmentPanel.this, "Download Cancelled !!!");
                }

            }
        });

		/*JProgressBar progressBar = new JProgressBar(0,100);
		progressBar.setStringPainted(true);
		progressBar.setValue(60);*/

        JToggleButton tglbtnSelectMultiple = new JToggleButton("Select Multiple");
        tglbtnSelectMultiple.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(tglbtnSelectMultiple.isSelected()){
                    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

                }
                else
                {
                    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                }
            }
        });
        //	tglbtnSelectMultiple.setBounds(10, 530, 200, 20);
        tglbtnSelectMultiple.setFont(new Font("Tahoma", Font.BOLD, 13));
        panel_2.add(tglbtnSelectMultiple);
        panel_2.add(downloadbtn);
        panel_2.add(refresh);
        //panel_2.add(progressBar);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 57, 1320, 450);
        add(scrollPane);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);
        table.setRowHeight(30);
    }
}
