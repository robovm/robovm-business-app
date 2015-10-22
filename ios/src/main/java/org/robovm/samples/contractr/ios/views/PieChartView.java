/*
 * Copyright (C) 2015 RoboVM AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.robovm.samples.contractr.ios.views;

import org.robovm.apple.coregraphics.*;
import org.robovm.apple.foundation.NSCoder;
import org.robovm.apple.uikit.UIBezierPath;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIGraphics;
import org.robovm.apple.uikit.UIView;
import org.robovm.objc.annotation.CustomClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
@CustomClass("PieChartView")
public class PieChartView extends UIView {

    public static class Component {
        final UIColor color;
        final double value;
        public Component(UIColor color, double value) {
            this.color = color;
            this.value = value;
        }
    }

    private ArrayList<Component> components = new ArrayList<>();

    public PieChartView(NSCoder coder) {
        super(coder);
    }

    public void setComponents(List<Component> components) {
        this.components.clear();
        this.components.addAll(components);
        setNeedsDisplay();
    }

    @Override
    public void draw(CGRect rect) {
        double diameter = Math.min(rect.getWidth(), rect.getHeight());
        double x = (rect.getWidth() - diameter) / 2.0;
        double y = (rect.getHeight() - diameter) / 2.0;
        double radius = diameter / 2.0;
        double originX = rect.getWidth() / 2.0;
        double originY = rect.getHeight() / 2.0;

        CGContext context = UIGraphics.getCurrentContext();
        context.setFillColor(UIColor.white().getCGColor());
        context.fillEllipseInRect(new CGRect(x, y, diameter, diameter));

        /*
         * Draw fills and outlines separately otherwise the last fill will render over the outline of the first segment
         * the line witdh of that segment will not be as wide as expected.
         */
        drawArcs(radius, originX, originY, true);
        drawArcs(radius, originX, originY, false);
    }

    private void drawArcs(double radius, double originX, double originY, boolean fill) {
        double sum = 0.0;
        for (Component c : components) {
            sum += c.value;
        }

        double startAngle = -Math.PI / 2.0;
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);

            double endAngle = startAngle + 2.0 * Math.PI * component.value / sum;

            UIBezierPath path = new UIBezierPath();
            path.move(new CGPoint(originX, originY));
            path.addArc(new CGPoint(originX, originY), radius, startAngle, endAngle, true);
            path.setLineWidth(2);

            if (fill) {
                /* Draw the filled arc. */
                component.color.setFill();
                path.fill();
            } else {
                /* Draw the white outline. */
                UIColor.white().setStroke();
                path.stroke();
            }

            startAngle = endAngle;
        }
    }
}
