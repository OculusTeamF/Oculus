/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.technical.printing;

import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.domain.entity.interfaces.IPrescriptionEntry;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by oculus on 11.05.15.
 */
public class Printer {

    private static final Printer printerInstance = new Printer();

    private static final int MAX_CHARACTERS_PER_LINE = 75;
    private static final int SPACING_LEFT = 80;

    private Printer (){

    }

    public static Printer getInstance (){
        return printerInstance;
    }

    public void print (String title, String text){

        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
        PDRectangle rectangle = page1.getMediaBox();
        document.addPage(page1);

        PDFont fontPlain = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;

        int line = 0;

        try {
            PDPageContentStream stream = new PDPageContentStream(document, page1);

            stream.beginText();
            stream.setFont(fontBold, 14);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - 50);
            stream.drawString(title + ":");
            stream.endText();

            int start = 0;
            int end = text.length();
            while (start < end){
                String tobePrinted;
                if ((end - start) > MAX_CHARACTERS_PER_LINE){
                    int tempEnd = start + MAX_CHARACTERS_PER_LINE;
                    while (text.charAt(tempEnd) != ' '){
                        ++tempEnd;
                    }
                    tobePrinted = text.substring(start, start = ++tempEnd);

                } else {
                    tobePrinted = text.substring(start);
                    start = start + MAX_CHARACTERS_PER_LINE;
                }
                stream.beginText();
                stream.setFont(fontPlain, 12);
                stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - 20 * (++line) - 60);
                stream.drawString(tobePrinted);
                stream.endText();
            }
            stream.close();

            document.save(title + ".pdf");
            document.close();
            Desktop.getDesktop().open(new File(title + ".pdf"));
        } catch (IOException | COSVisitorException e) {
            //TODO
        }
    }

    public void printPrescription (IPrescription iPrescription){
        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
        PDRectangle rectangle = page1.getMediaBox();
        document.addPage(page1);

        PDFont fontPlain = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;

        int line = 0;

        try {
            PDPageContentStream stream = new PDPageContentStream(document, page1);

            stream.beginText();
            stream.setFont(fontBold, 14);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - 50);
            stream.drawString("Prescription:");
            stream.endText();

            IPatient iPatient = iPrescription.getPatient();

            stream.beginText();
            stream.setFont(fontPlain, 12);
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - 20 * (++line) - 60);
            stream.drawString(iPatient.getFirstName() + " " + iPatient.getLastName());
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - 20 * (++line) - 60);
            stream.drawString(iPatient.getBirthDay().toString());
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - 20 * (++line) - 60);
            stream.drawString(iPatient.getStreet());
            stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - 20 * (++line) - 60);
            stream.drawString(iPatient.getPostalCode() + ", " + iPatient.getCity());
            stream.endText();
            
            for (IPrescriptionEntry entry : iPrescription.getPrescriptionEntries()){
                stream.beginText();
                stream.setFont(fontPlain, 12);
                stream.moveTextPositionByAmount(SPACING_LEFT, rectangle.getHeight() - 20 * (++line) - 60);
                stream.drawString("ID: " + entry.getId() + ", " + entry.getMedicine());
                stream.endText();
            }
            stream.close();

            document.save("prescription.pdf");
            document.close();
            Desktop.getDesktop().open(new File("prescription.pdf"));
        } catch (IOException | COSVisitorException e) {
            //TODO
        }

    }
}
