/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.models;

/**
 *
 * @author ANGEL NAVARRO
 */
public class ModelPoint {

    private String X;
    private String Y;

    public String getX() {
        return X;
    }

    public void setX(String X) {
        this.X = X;
    }

    public String getY() {
        return Y;
    }

    public void setY(String Y) {
        this.Y = Y;
    }

    @Override
    public String toString() {
        return "ModelPoint{" + "X=" + X + ", Y=" + Y + '}';
    }

}
