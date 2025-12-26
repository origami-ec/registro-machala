/*
 * Copyright (C) 2020 
 * Authors: Ricardo Arguello, Misael Fernández
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.*
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.bcbg.rubrica.sign.pdf;

import java.util.Properties;
import java.util.logging.Logger;

import com.lowagie.text.Rectangle;

public class PdfUtil {

    public static final String POSITION_ON_PAGE_LOWER_LEFT_X = "PositionOnPageLowerLeftX";
    public static final String POSITION_ON_PAGE_LOWER_LEFT_Y = "PositionOnPageLowerLeftY";
    public static final String POSITION_ON_PAGE_UPPER_RIGHT_X = "PositionOnPageUpperRightX";
    public static final String POSITION_ON_PAGE_UPPER_RIGHT_Y = "PositionOnPageUpperRightY";

    private static final Logger logger = Logger.getLogger(PdfUtil.class.getName());

    public static Rectangle getPositionOnPage(Properties extraParams) {
        if (extraParams == null) {
            logger.severe("Se ha pedido una posicion para un elemento grafico nulo");
            return null;
        } else {
            if (extraParams.getProperty("PositionOnPageLowerLeftX") != null && extraParams.getProperty("PositionOnPageLowerLeftY") != null && extraParams.getProperty("PositionOnPageUpperRightX") != null && extraParams.getProperty("PositionOnPageUpperRightY") != null) {
                try {
                    return new Rectangle((float)Integer.parseInt(extraParams.getProperty("PositionOnPageLowerLeftX").trim()), (float)Integer.parseInt(extraParams.getProperty("PositionOnPageLowerLeftY").trim()), (float)Integer.parseInt(extraParams.getProperty("PositionOnPageUpperRightX").trim()), (float)Integer.parseInt(extraParams.getProperty("PositionOnPageUpperRightY").trim()));
                } catch (Exception var3) {
                    logger.severe("Se ha indicado una posicion invalida para la firma: " + var3);
                }
            }

            if (extraParams.getProperty("PositionOnPageLowerLeftX") != null && extraParams.getProperty("PositionOnPageLowerLeftY") != null && extraParams.getProperty("PositionOnPageUpperRightX") == null && extraParams.getProperty("PositionOnPageUpperRightY") == null) {
                try {
                    return new Rectangle((float)Integer.parseInt(extraParams.getProperty("PositionOnPageLowerLeftX").trim()), (float)Integer.parseInt(extraParams.getProperty("PositionOnPageLowerLeftY").trim()), (float)(Integer.parseInt(extraParams.getProperty("PositionOnPageLowerLeftX").trim()) + 110), (float)(Integer.parseInt(extraParams.getProperty("PositionOnPageLowerLeftY").trim()) - 36));
                } catch (Exception var2) {
                    logger.severe("Se ha indicado una posicion invalida para la firma: " + var2);
                }
            }

            return null;
        }
    }
}
