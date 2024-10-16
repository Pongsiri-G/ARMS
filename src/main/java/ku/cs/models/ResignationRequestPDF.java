package ku.cs.models;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResignationRequestPDF {
    private static final String imagePath = "src/main/resources/images/KU.png";
    private static final String thaiFontPath = "src/main/resources/style/THSarabunNew.ttf";

    public static void createRequest(String requestsDirectory, ResignationRequest request) throws IOException {
        Student student = request.getRequester();
        // Create PDF document
        File file = new File(requestsDirectory);
        file.getParentFile().mkdirs();
        PdfWriter writer = new PdfWriter(requestsDirectory);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        // Set Font
        PdfFont thaiFont = PdfFontFactory.createFont(thaiFontPath,"Identity-H", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

        // Get PageSize
        Rectangle pageSize = pdfDoc.getDefaultPageSize();
        float width = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();
        float height = pageSize.getHeight() - document.getTopMargin() - document.getBottomMargin();

        // Create a Div to contain Element
        Div div = new Div();

        // Adding the image to the top left of the document
        ImageData imageData = ImageDataFactory.create(imagePath);
        Image image = new Image(imageData);
        image.setHeight(70);
        image.setWidth(70);

        // Create Header next to Image
        Paragraph textHeader = new Paragraph()
                .setFixedLeading(14)
                .add(new Paragraph().add("มหาวิทยาลัยเกษตรศาสตร์\n").setFont(thaiFont).setFontSize(24).setBold().setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph().add("ใบขอลาออก /Resignation Form").setFont(thaiFont).setFontSize(18).setTextAlignment(TextAlignment.LEFT));

        //div.setHorizontalAlignment(HorizontalAlignment.LEFT); // Align elements horizontally to the left

        // Create a table with 2 columns
        Table headerTable = new Table(2)
                .addCell(new Cell().add(image).setBorder(Border.NO_BORDER))
                .addCell(new Cell().add(textHeader).setBorder(Border.NO_BORDER).setPaddingLeft(10).setPaddingTop(15)); // Adjust the number of columns as needed d the image to the div
        div.add(headerTable);    // Add the header to the div

        // Use a layout strategy to align them side by side
        div.setMarginBottom(20); // Add some margin below the div if needed

        // Date
        Paragraph textDateRight = new Paragraph(
                "วันที่ " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy")) + "\n" +
                        "Date               DD/MM/YY")
                .setPaddingTop(-20)
                .setFixedLeading(14)
                .setTextAlignment(TextAlignment.RIGHT)
                //.setHorizontalAlignment(HorizontalAlignment.RIGHT)
                .setFont(thaiFont)
                .setFontSize(14);
        div.add(textDateRight);


        Paragraph toAdvisor = new Paragraph()
                .add(new Paragraph()
                        .setFixedLeading(14)
                        .add("เรียน: อาจารย์" + student.getStudentAdvisor().getName() + "\n").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT)
                        .add("To     (อาจารย์ที่ปรึกษา/Advisor)\n").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
                );


        Paragraph nisitName = new Paragraph()
                .setFixedLeading(4)
                .add(new Paragraph("ชื่อนิสิต (นาย/นาง/นางสาว): " + student.getName()).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add(new Tab())
                .addTabStops(new TabStop(1000, TabAlignment.RIGHT))
                .add(new Paragraph("ตัวบรรจง").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.RIGHT))
                .add("\n")
                .add(new Paragraph("Student’s name (Mr./Mrs./Miss)").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT))
                .add(new Tab())
                .addTabStops(new TabStop(1000, TabAlignment.RIGHT))
                .add(new Paragraph("(Print Name)").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        Paragraph nisitInfo1 = new Paragraph()
                .setFixedLeading(4)
                .add(new Paragraph("รหัสประจำตัว: " + student.getStudentID()).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add(new Tab())
                .addTabStops(new TabStop(225, TabAlignment.LEFT))
                //.add(new Paragraph("ชั้นปีที่").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                //.add(new Tab())
                //.addTabStops(new TabStop(350, TabAlignment.LEFT))
                .add(new Paragraph("คณะ: " + student.getEnrolledFaculty().getFacultyName()).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("Student ID Number").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT))
                .add(new Tab())
                .addTabStops(new TabStop(350, TabAlignment.LEFT))
                .add(new Paragraph("Faculty").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        Paragraph nisitInfo2 = new Paragraph()
                .setFixedLeading(4)
                .add(new Paragraph("สาขาวิชาเอก: " + student.getEnrolledDepartment().getDepartmentName()).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add(new Tab())
                .addTabStops(new TabStop(200, TabAlignment.LEFT))
                .add(new Paragraph("หมายเลขโทรศัพท์: " + request.getNumberPhone()).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("Major Field").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT))
                .add(new Tab())
                .addTabStops(new TabStop(200, TabAlignment.LEFT))
                .add(new Paragraph("Phone number").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT))
                .add(new Tab())
                .addTabStops(new TabStop(350, TabAlignment.LEFT));


        Paragraph nisitInfo3 = new Paragraph()
                .setFixedLeading(4)
                .add(new Paragraph("มีความประสงค์ขอลาออก เนื่องจาก: ").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("Reason(s) for Resignation ").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT))
                .add("\n");

        Paragraph nisitReason = new Paragraph().add(request.getReason()).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT);

        Paragraph nisitInfo4 = new Paragraph()
                .setFixedLeading(4)

                .add(new Paragraph("จึงขอลาออกตั้งแต่บัดนี้เป็นต้นไป และข้าพเจ้าไม่มีหนี้สินค้างชำระ").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("This resignation is effective immediately. I have no outstanding debt.").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        Paragraph studentDigitalSignature = new Paragraph()
                .setTextAlignment(TextAlignment.RIGHT)
                .setFixedLeading(10)
                .add(new Paragraph("ลายมือชื่อดิจิทัลนี้ถูกลงชื่อจากระบบคำร้อง\nโดย " + request.getRequester().getName() + " เมื่อวันที่ " + request.getTimestamp() ).setFont(thaiFont).setFontSize(10).setFixedLeading(10)); //ลายเซ็นนิสิต

        Paragraph studentSignature = new Paragraph()
                .setTextAlignment(TextAlignment.RIGHT)
                .add(new Paragraph("ลงนามนิสิต/ผู้ดำเนินการแทน " + request.getRequester().getName() + "\nStudent/Person Requesting Signature").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT).setFixedLeading(14));

        Paragraph headOfDepartment = new Paragraph()
                .setFixedLeading(5)
                .add(new Paragraph("1) เรียน หัวหน้าภาค").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT).setPaddingTop(5))
                .add("\n")
                .add(new Paragraph("To Head of department").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("อนุมัติ (Approved)").setPaddingLeft(20).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                //.add("\n")
                //.add(new Paragraph("[ ] ไม่อนุมัติ Denied").setPaddingLeft(20).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("ลงนาม/ Signature " + request.getRequester().getStudentAdvisor().getName()).setFixedLeading(20).setPaddingLeft(60).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.RIGHT)
                        .add("\n")
                        .add("( " + request.getRequester().getStudentAdvisor().getName() + " )")
                        .add("\n")
                        .add("" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy")))
                        .add("\n")
                        .add("อาจารย์ที่ปรึกษา Advisor    "))
                .add(new Paragraph("ลายมือชื่อดิจิทัลนี้ถูกลงชื่อจากระบบคำร้อง\nโดย " + request.getRequester().getStudentAdvisor().getName() + " เมื่อวันที่ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ).setTextAlignment(TextAlignment.LEFT).setFont(thaiFont).setFontSize(10).setFixedLeading(10));

        Paragraph dean = new Paragraph()
                .setFixedLeading(5)
                .add(new Paragraph("2) เรียน คณบดี").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT).setPaddingTop(5))
                .add("\n")
                .add(new Paragraph("To Dean").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("[ ] อนุมัติ Approved").setPaddingLeft(20).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("[ ] ไม่อนุมัติ Denied").setPaddingLeft(20).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("ลงนาม/ Signature_______________").setFixedLeading(20).setPaddingLeft(60).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.RIGHT)
                        .add("\n")
                        .add("(_______________)")
                        .add("\n")
                        .add("_____/_____/_____")
                        .add("\n")
                        .add("หัวหน้าภาควิชา Head of department"));

        Paragraph deanDecision = new Paragraph()
                .setFixedLeading(5)
                .add(new Paragraph("3) คำพิจารณาคณบดีเจ้าสังกัด").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT).setPaddingTop(5))
                .add("\n")
                .add(new Paragraph("Dean’s decision\n").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("[ ] อนุมัติ Approved").setPaddingLeft(20).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("[ ] ไม่อนุมัติ Denied").setPaddingLeft(20).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("ลงนาม/ Signature_______________").setFixedLeading(20).setPaddingLeft(60).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.RIGHT)
                        .add("\n")
                        .add("(_______________)")
                        .add("\n")
                        .add("_____/_____/_____")
                        .add("\n")
                        .add("  คณบดี Dean  "));

        Paragraph directorAdmin = new Paragraph()
                .setFixedLeading(5)
                .add(new Paragraph("4) เรียน ผู้อำนวยการสำนักบริหารการศึกษา\n").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT).setPaddingTop(5))
                .add("\n")
                .add(new Paragraph("To Director of Office of Educational Administration").setFont(thaiFont).setFontSize(10).setTextAlignment(TextAlignment.LEFT))
                .add("\n")
                .add(new Paragraph("เพื่อโปรดดำเนินการ").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.CENTER))
                .add("\n")
                .add(new Paragraph("To be proceeded").setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.CENTER))
                .add("\n")
                .add(new Paragraph("ลงนาม/ Signature_______________").setFixedLeading(20).setPaddingLeft(60).setFont(thaiFont).setFontSize(14).setTextAlignment(TextAlignment.RIGHT)
                        .add("\n")
                        .add("(_______________)")
                        .add("\n")
                        .add("_____/_____/_____"));

        Table approvalTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        approvalTable.setKeepTogether(true);
        approvalTable.setWidth(UnitValue.createPercentValue(100));


        approvalTable.addCell(headOfDepartment);
        approvalTable.addCell(dean);
        approvalTable.addCell(deanDecision);
        approvalTable.addCell(directorAdmin);



        div.add(toAdvisor);
        div.add(nisitName);
        div.add(nisitInfo1);
        div.add(nisitInfo2);
        div.add(nisitInfo3);
        div.add(nisitReason);
        div.add(nisitInfo4);
        div.add(studentDigitalSignature);
        div.add(studentSignature);
        div.add(approvalTable);
        // Add the div to the document
        document.add(div);

        // Close the document
        document.close();
    }
}
