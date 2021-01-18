package com.verygood.security.example.tessellation.model;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlElement;

public class Vertex {

  @JsonbProperty("x")
  @XmlElement(name = "x")
  private final double x;

  @JsonbProperty("y")
  @XmlElement(name = "x")
  private final double y;

  @JsonbProperty("z")
  @XmlElement(name = "x")
  private final double z;

  @JsonbCreator
  public Vertex(@JsonbProperty("x") final double x, @JsonbProperty("y") final double y, @JsonbProperty("z") final double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

}
