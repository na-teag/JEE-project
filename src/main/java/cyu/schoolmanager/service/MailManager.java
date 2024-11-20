package cyu.schoolmanager.service;

import cyu.schoolmanager.*;

import java.util.*;

public class MailManager {
    private static MailManager instance;

    private MailManager() {}

    public static synchronized MailManager getInstance() {
        if (instance == null) {
            instance = new MailManager();
        }
        return instance;
    }



    private void sendEmailToPerson(String senderEmail, Person person, String object, String body){
        System.out.println(senderEmail + " to " + person.getEmail() + " : " + object + "\n " + body);
    }

    private void sendEmailToGroup(String senderEmail, StudentGroup studentGroup, String object, String body) {
        PersonManager personManager = PersonManager.getInstance();
        ClasseManager classeManager = ClasseManager.getInstance();
        List<Classe> classeList = classeManager.getClassesByStudentGroup(studentGroup);
        List<Student> studentList = new ArrayList<>();
        for (Classe classe : classeList) {
            studentList.addAll(personManager.getStudentsFromClasse(classe));
        }
        Set<Long> seenIds = new HashSet<>();
        List<Student> uniqueStudents = new ArrayList<>();

        // delete the potential duplicates
        for (Student student : studentList) {
            if (seenIds.add(student.getId())) {
                uniqueStudents.add(student);
            }
        }
        for (Student student : uniqueStudents){
            sendEmailToPerson(senderEmail, student, object, body);
        }
    }


    public void sendEmail(String senderEmail, Emailable emailable, String object, String body){
        if (emailable.getClass().getName().equals(Student.class.getName()) || emailable.getClass().getName().equals(Professor.class.getName()) || emailable.getClass().getName().equals(Admin.class.getName())){
            sendEmailToPerson(senderEmail, (Person)emailable, object, body);
        } else if (emailable.getClass().getName().equals(Promo.class.getName()) || emailable.getClass().getName().equals(Pathway.class.getName()) || emailable.getClass().getName().equals(Classe.class.getName())) {
            sendEmailToGroup(senderEmail, (StudentGroup) emailable, object, body);
        }
    }

    public void sendEmail(String senderEmail, List<Emailable> emailableList, String object, String body) {
        for (Emailable emailable : emailableList){
            sendEmail(senderEmail, emailable, object, body);
        }
    }
}