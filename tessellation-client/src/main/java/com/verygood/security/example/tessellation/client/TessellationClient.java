package com.verygood.security.example.tessellation.client;

import com.verygood.security.example.tessellation.model.Polygon;
import com.verygood.security.example.tessellation.model.Triangle;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface TessellationClient {

  @NotNull
  Stream<@NotNull Triangle> tessellate(@NotNull Stream<@NotNull Polygon> polygons);

}
