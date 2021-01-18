package com.verygood.security.example.tessellation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

public class Triangle {

  @JsonbProperty("vertices")
  @XmlElement(name = "vertices")
  @NotNull
  @Size(min = 3, max = 3)
  private final List<@NotNull Vertex> vertices;

  @JsonbCreator
  public Triangle(@JsonbProperty("vertices") @NotNull @Size(min = 3, max = 3) final List<@NotNull Vertex> vertices) {
    this.vertices = Collections.unmodifiableList(new ArrayList<>(vertices));
  }

  @NotNull
  @Size(min = 3, max = 3)
  public List<Vertex> getVertices() {
    return vertices;
  }
}
