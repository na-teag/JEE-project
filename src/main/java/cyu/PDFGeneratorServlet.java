package cyu;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import cyu.schoolmanager.Course;
import cyu.schoolmanager.Grade;
import cyu.schoolmanager.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PDFGeneratorServlet", urlPatterns = "/pdf")
public class PDFGeneratorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Vérifie que la personne est connectée et que c'est un étudiant
        if (session.getAttribute("user") != null && session.getAttribute("role").equals(Student.class.getName())) {

            // Récupérer les informations nécessaires
            List<Course> courses = (List<Course>) session.getAttribute("courses");
            List<Grade> grades = (List<Grade>) session.getAttribute("grades");
            Student student = (Student) session.getAttribute("user");

            // Définir le type de contenu et le nom du fichier
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Releve_De_Note_" +
                    student.getFirstName() + "_" + student.getLastName() + ".pdf");

            // Création d'un document PDF avec OpenPDF
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.setMargins(10, 10, 36, 36);

            document.open();


            // Ajouter un titre
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Relevé de Notes", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            //Ajouter les informations de l'étudiant
            Paragraph studentParagraph = new Paragraph("                 " + student.getFirstName() + " " + student.getLastName());
            document.add(studentParagraph);
            Paragraph numberParagraphe = new Paragraph("                 " + "N° étudiant : " + student.getPersonNumber());
            document.add(numberParagraphe);

            document.add(new Paragraph("\n"));

            // Ajouter la moyenne générale (si il y a des notes disponibles)
            Double average = (Double) session.getAttribute("average");
            if (average != null) {
                Paragraph avgParagraph = new Paragraph("                 Moyenne générale: " + String.format("%.2f", average));
                document.add(avgParagraph);
            } else {
                Paragraph noAvg = new Paragraph("                 Aucune note disponible");
                noAvg.setIndentationRight(50);
                document.add(noAvg);
            }

            document.add(new Paragraph(" "));

            // Ajouter la table des notes
            PdfPTable table = new PdfPTable(5);

            // Définir les entêtes de la table
            table.addCell(createPdfPcell("Cours"));
            table.addCell(createPdfPcell("Note"));
            table.addCell(createPdfPcell("Contexte"));
            table.addCell(createPdfPcell("Commentaire"));
            table.addCell(createPdfPcell("Session"));

            // Ajouter les lignes de la table
            if (grades != null && !grades.isEmpty()) {
                for (Grade grade : grades) {
                    table.addCell(createPdfPcell(grade.getCourse().getSubject().getName()));
                    table.addCell(createPdfPcell(String.valueOf(grade.getResult())));
                    table.addCell(createPdfPcell(grade.getContext()));
                    table.addCell(createPdfPcell(grade.getComment()));
                    table.addCell(createPdfPcell(String.valueOf(grade.getSession())));
                }
            } else {
                // Si aucune note, afficher uniquement les cours
                for (Course course : courses) {
                    table.addCell(createPdfPcell(course.getSubject().getName()));
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                }
            }

            document.add(table);

            document.close();
        } else {
            // Si l'utilisateur n'est pas connecté ou n'est pas un étudiant, on redirige vers la page d'erreur
            request.getRequestDispatcher("WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private PdfPCell createPdfPcell(String content) {
        Font font = new Font(Font.HELVETICA, 10, Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setMinimumHeight(16);
        cell.setPadding(8);
        return cell;
    }
}
