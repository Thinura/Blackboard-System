package main.instructor;

import javax.swing.JPanel;

import main.student.coursepanel.CourseInfoPanel;

public class InstructorInfoPanel extends  CourseInfoPanel{
    /**
     * Create the panel.
     */
    public InstructorInfoPanel()throws Exception {
        super();
    }
    public InstructorInfoPanel(String course_id)throws Exception {
        super(course_id);
    }
}
