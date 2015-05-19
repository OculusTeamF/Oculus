/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * <h1>$Printer.java</h1>
 *
 * @author $sha9939
 * @since $12.05.15
 * <p/>
 * Description:
 * This file contains the class Printer and all its methods and functionality to print information and save it in a PDF
 * file.
 */
        package at.oculus.teamf.technical.printing;

        import at.oculus.teamf.domain.entity.exception.CantGetPresciptionEntriesException;
        import at.oculus.teamf.domain.entity.interfaces.IDoctor;
        import at.oculus.teamf.domain.entity.interfaces.IPatient;
        import at.oculus.teamf.domain.entity.interfaces.IPrescription;
        import at.oculus.teamf.domain.entity.interfaces.IPrescriptionEntry;
        import at.oculus.teamf.technical.exceptions.NoPrescriptionToPrintException;
        import at.oculus.teamf.technical.loggin.ILogger;
        import org.apache.pdfbox.exceptions.COSVisitorException;
        import org.apache.pdfbox.pdmodel.PDDocument;
        import org.apache.pdfbox.pdmodel.PDPage;
        import org.apache.pdfbox.pdmodel.common.PDRectangle;
        import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
        import org.apache.pdfbox.pdmodel.font.PDFont;
        import org.apache.pdfbox.pdmodel.font.PDType1Font;
        import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
        import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

        import javax.imageio.ImageIO;
        import java.awt.*;
        import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.IOException;
        import java.sql.Timestamp;
        import java.util.Date;
        import java.util.Random;

/**
 * <h2>$Printer</h2>
 * <p/>
 * <b>Description:</b>
 * This class implements the Printer with all its methods and functionality. It provides methods to create PDF-Files
 * and save / print them.
 */
public class Printer implements ILogger{

    private static final Printer printerInstance = new Printer();

    private static final int MAX_CHARACTERS_PER_LINE = 75;
    private static final int SPACING_LEFT = 80;
    private static final int SPACING_TOP = 60;
    private static final int SPACING_HEADER = 70;
    private static final int LINE_HEIGHT = 20;

    /**
     * <h3>$Printer</h3>
     * <p/>
     * <b>Description:</b>
     * Basic private Constructor to help implement the Singleton.
     */
    private Printer() {

    }

    /**
     * <h3>$getInstance</h3>
     * <p/>
     * <b>Description:</b>
     * Get an instance of the Printer. Singleton.
     */
    public static Printer getInstance() {
        return printerInstance;
    }

    /**
     * <h3>$print</h3>
     * <p/>
     * <b>Description:</b>
     * <p/>
     * This method prints the given parameters into a PDF-Document and opens the file with the standard program.
     * <p/>
     * <b>Parameter</b>
     *
     * @param title Is a string which will be printed as title in the PDF Document.
     * @param text Is a string which will be printed as message in the PDF Document.
     */
    public void print(String title, String text) throws IOException, COSVisitorException {

        //creates a new PDDocument object
        PDDocument document = new PDDocument();
        //create a page to the document
        PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
        //rectangle is for sizes (height and width) of page
        PDRectangle rectangle = page1.getMediaBox();
        //add page to document
        document.addPage(page1);

        //fonts for the document are implemented here
        PDFont fontPlain = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;

        //running variable to calculate which line you are in the document right now
        int line = 0;

        try {
            //create the content stream which will write into the document
            PDPageContentStream stream = new PDPageContentStream(document, page1);

            //always use all these lines for entering new text into the document
            //move textToPosition will set the cursor to the current position
            stream.beginText();
            stream.setFont(fontBold, 14);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - SPACING_TOP);
            stream.drawString(title + ":");
            stream.endText();

            //calculates the correct place and then writes the whole text into the PDF-Document
            int start = 0;
            int end = text.length();
            while (start < end) {
                String tobePrinted;
                if ((end - start) > MAX_CHARACTERS_PER_LINE) {
                    int tempEnd = start + MAX_CHARACTERS_PER_LINE;
                    while (text.charAt(tempEnd) != ' ') {
                        ++tempEnd;
                    }
                    tobePrinted = text.substring(start, start = ++tempEnd);

                } else {
                    tobePrinted = text.substring(start);
                    start = start + MAX_CHARACTERS_PER_LINE;
                }
                stream.beginText();
                stream.setFont(fontPlain, 12);
                stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
                stream.drawString(tobePrinted);
                stream.endText();

            }
            //print oculus image into pdf file
            /*
            BufferedImage awtImage = ImageIO.read(new File("/home/oculus/IdeaProjects/Oculus/Technical/src/res/oculus.JPG"));
            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            float scale = 0.3f; // alter this value to set the image size
            stream.drawXObject(ximage, 380, 780, ximage.getWidth() * scale, ximage.getHeight() * scale);
            */

            //close stream afterwards
            stream.close();

            //save document and close document
            document.save("/home/oculus/IdeaProjects/Oculus/Technical/src/at/oculus/teamf/technical/printing/" + title + ".pdf");
            document.close();
            //open document with standard application for the file
            Desktop.getDesktop().open(new File("/home/oculus/IdeaProjects/Oculus/Technical/src/at/oculus/teamf/technical/printing/" + title + ".pdf"));
        } catch (IOException | COSVisitorException e) {
            throw e;
        }
    }

    /**
     * <h3>$printPrescription</h3>
     * <p/>
     * <b>Description:</b>
     * <p/>
     * This method prints the given parameters into a PDF-Document and opens the file with the standard program.
     * <p/>
     * <b>Parameter</b>
     *
     * @param iPrescription Is an Interface of a prescription from which all the information will be printed into
     * a PDF-File and then started with standard application from OS.
     */
    public void printPrescription(IPrescription iPrescription, IDoctor iDoctor) throws COSVisitorException, IOException, CantGetPresciptionEntriesException, NoPrescriptionToPrintException {
        if(iPrescription==null){
            throw new NoPrescriptionToPrintException();
        }

        //instantiate a new document, create a page and add the page to the document
        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
        PDRectangle rectangle = page1.getMediaBox();
        document.addPage(page1);
        log.info("Document created.");

        //create fonts for the document
        PDFont fontPlain = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        log.info("Fonts added.");

        //running variable to calculate current line
        int line = 0;

        try {
            //new Stream to print into the file
            PDPageContentStream stream = new PDPageContentStream(document, page1);

            //print header (headlining)
            stream.beginText();
            stream.setFont(fontBold, 14);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - SPACING_TOP);
            stream.drawString("Prescription:");
            stream.endText();

            //get patient to print data from
            IPatient iPatient = iPrescription.getPatient();

            //write data from patient
            stream.beginText();
            stream.setFont(fontPlain, 12);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
            stream.drawString(iPatient.getFirstName() + " " + iPatient.getLastName());
            stream.endText();

            stream.beginText();
            stream.setFont(fontPlain, 12);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
            stream.drawString(iPatient.getBirthDay().toString());
            stream.endText();

            if (iPatient.getStreet() != null){
                stream.beginText();
                stream.setFont(fontPlain, 12);
                stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
                stream.drawString(iPatient.getStreet());
                stream.endText();
            }

            if ((iPatient.getPostalCode() != null) && (iPatient.getCity() != null)){
                stream.beginText();
                stream.setFont(fontPlain, 12);
                stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
                stream.drawString(iPatient.getPostalCode() + ", " + iPatient.getCity());
                stream.endText();
            }
            log.info("Patient printed.");

            //next row
            ++line;

            //write data from doctor
            stream.beginText();
            stream.setFont(fontBold, 14);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
            stream.drawString("Prescription issued from doctor:");
            stream.endText();

            stream.beginText();
            stream.setFont(fontPlain, 12);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
            stream.drawString(iDoctor.getTitle() + " " + iDoctor.getFirstName() + " " + iDoctor.getLastName());
            stream.endText();

            //next row
            ++line;
            log.info("Doctor printed.");

            //print all the entries in the prescription
            if (iPrescription.getPrescriptionEntries().size() > 0) {
                stream.beginText();
                stream.setFont(fontBold, 14);
                stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
                stream.drawString("Prescription Entries:");
                stream.endText();
                for (IPrescriptionEntry entry : iPrescription.getPrescriptionEntries()) {
                    stream.beginText();
                    stream.setFont(fontPlain, 12);
                    stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
                    stream.drawString("ID: " + entry.getId() + ", " + entry.getMedicine());
                    stream.endText();
                }
            }
            log.info("Medicine entries printed.");

            //print oculus image into pdf file
            //Not working


            BufferedImage awtImage = ImageIO.read(new File("Technical/res/oculus.JPG"));
            //filename for tests
            //BufferedImage awtImage = ImageIO.read(new File("../Technical/res/oculus.JPG"));
            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            float scale = 0.3f; // alter this value to set the image size
            stream.drawXObject(ximage, 380, 780, ximage.getWidth() * scale, ximage.getHeight() * scale);


            //signature field
            ++line;
            stream.beginText();
            stream.setFont(fontPlain, 12);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (++line) - SPACING_HEADER);
            stream.drawString("Doctor's Signature:");
            stream.endText();

            //print a line for signature field
            stream.setLineWidth(0.5f);
            stream.addLine(SPACING_LEFT, rectangle.getHeight() - LINE_HEIGHT * (line += 2) - SPACING_HEADER, SPACING_LEFT + 100, rectangle.getHeight() - LINE_HEIGHT * (line) - SPACING_HEADER);
            stream.closeAndStroke();
            log.info("Signature field printed");

            //close the stream
            stream.close();

            //save the document and close it
            Timestamp tstamp = new Timestamp(new Date().getTime());
            System.out.println(System.getProperty("user.dir"));
            String tstampString = tstamp.toString();
            tstampString = tstampString.replace(' ', '_');
            tstampString = tstampString.replace(':', '_');
            String finalFileName = tstampString.substring(0, 19);
            String filename = "Technical/output/prescription" + finalFileName + ".pdf";

            //filename for tests
            //String filename = "../Technical/output/prescription_" + tstamp.toString() + ".pdf";
            document.save(filename);
            document.close();
            log.info("Document saved with filename: " + filename);

            //open file with standard OS application
            Desktop.getDesktop().open(new File(filename));
            log.info("Document opened.");

        } catch (COSVisitorException | CantGetPresciptionEntriesException | IOException e) {
            throw e;
        }
    }
}