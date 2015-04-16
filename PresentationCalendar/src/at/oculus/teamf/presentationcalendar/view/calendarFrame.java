/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentationcalendar.view;

/**
 * MiG Calendar Test
 *
 * @author Fabian Salzgeber
 * @date 13.4.2015
 */

import com.miginfocom.calendar.activity.Activity;
import com.miginfocom.calendar.activity.ActivityDepository;
import com.miginfocom.calendar.activity.DefaultActivity;
import com.miginfocom.calendar.datearea.ThemeDateAreaContainer;
import com.miginfocom.calendar.grid.GridLineRepetition;
import com.miginfocom.calendar.header.CellDecorationRow;
import com.miginfocom.calendar.theme.CalendarTheme;
import com.miginfocom.theme.Theme;
import com.miginfocom.theme.Themes;
import com.miginfocom.util.dates.DateFormatList;
import com.miginfocom.util.dates.DateRange;
import com.miginfocom.util.dates.ImmutableDateRange;
import com.miginfocom.util.gfx.ShapeGradientPaint;
import com.miginfocom.util.gfx.geometry.AbsRect;
import com.miginfocom.util.gfx.geometry.numbers.AtFixed;
import com.miginfocom.util.repetition.DefaultRepetition;

import javax.swing.*;
import java.awt.*;
import java.util.GregorianCalendar;

public class calendarFrame extends JPanel
{

    public calendarFrame() {
        GridLayout exLayout = new GridLayout(1,1);
        this.setLayout(exLayout);

        this.setBackground(Color.blue); // for debug

        ThemeDateAreaContainer container = new ThemeDateAreaContainer("myContext");

        long startMillis = new GregorianCalendar(215, 1, 24, 22, 59, 00).getTimeInMillis();
        long endMillis = new GregorianCalendar(216, 1, 25, 23, 00, 00).getTimeInMillis();
        DateRange visibleRange = new DateRange(startMillis, endMillis, true, null, null);
        container.getDateArea().setVisibleDateRange(visibleRange);

        Theme theme = Themes.getTheme("myContext");
        theme.putValue(CalendarTheme.KEY_GENERIC_BACKGROUND, Color.white);

        Theme ctheme = new CalendarTheme("myContext");

        ShapeGradientPaint headerBackground = new ShapeGradientPaint(
                new Color(235, 235, 235),
                new Color(255, 255, 255),
                90, 0.7f, 0.6f, false
        );
        CellDecorationRow headerRow = new CellDecorationRow(
                DateRange.RANGE_TYPE_DAY,
                new DateFormatList("E' 'dd'/'M"),
                new AtFixed(20f),
                AbsRect.FILL,
                headerBackground,
                Color.DARK_GRAY,
                new DefaultRepetition(),
                new Font("SansSerif", Font.PLAIN, 12)
        );

        GridLineRepetition gridLines = new GridLineRepetition(1, new Color(220, 220, 220));
        String mainKey = CalendarTheme.KEY_HEADER_;
        ctheme.addToList(mainKey + "North/CellDecorationRows#", headerRow);
        ctheme.addToList(mainKey + "North/GridLines/PrimaryDim#", gridLines);
        container = new ThemeDateAreaContainer("myContext");


        ImmutableDateRange actRange = new ImmutableDateRange(
                System.currentTimeMillis(), DateRange.RANGE_TYPE_HOUR, 4, null, null
        );
        Activity activity = new DefaultActivity(actRange, new Integer(1234));
        activity.setSummary("Doktor Krebs");
        ActivityDepository.getInstance().addBrokedActivity(activity, null);

        container.getDateArea().setActivitiesSupported(true);

        add(container);
    }


}
