package com.verygood.security.example.tessellation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Polygon {

  @JsonbProperty("vertices")
  @NotNull
  @Size(min = 3)
  private final List<@NotNull Vertex> vertices;

  @JsonbCreator
  public Polygon(
      @JsonbProperty("vertices")
      @NotNull @Size(min = 3) final List<@NotNull Vertex> vertices) {
    this.vertices = Collections.unmodifiableList(new ArrayList<>(vertices));;
  }

  @NotNull
  @Size(min = 3)
  public List<Vertex> getVertices() {
    return vertices;
  }

}
