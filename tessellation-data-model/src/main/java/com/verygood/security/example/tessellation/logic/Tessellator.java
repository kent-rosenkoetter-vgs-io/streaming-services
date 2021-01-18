package com.verygood.security.example.tessellation.logic;

import com.verygood.security.example.tessellation.model.Polygon;
import com.verygood.security.example.tessellation.model.Triangle;
import java.util.Collection;
import java.util.stream.Stream;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A service that tessellates polygons into triangles.
 */
public interface Tessellator {

  /**
   * Tessellates one polygon into triangles.
   *
   * @param polygon a single polygon
   * @return a collection of triangles covering the same planar area as the source polygon
   */
  @NotEmpty
  Collection<@NotNull Triangle> tessellatePolygon(@NotNull Polygon polygon);

  /**
   * Tessellates a stream of polygons into a stream of triangles suitable for rendering.
   *
   * @param polygonStream a stream of polygons
   * @return a stream of equivalent triangles
   */
  @NotNull
  Stream<@NotNull Triangle> tessellatePolygons(@NotNull Stream<@NotNull Polygon> polygonStream);

}
