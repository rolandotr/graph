/*
 * Copyright © 2014 - 2018 Leipzig University (Database Research Group)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradoop.flink.model.impl.operators.matching.single.cypher.operators.expand.tuples;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.flink.api.java.tuple.Tuple3;
import org.gradoop.common.model.impl.id.GradoopId;
import org.gradoop.flink.model.impl.operators.matching.single.cypher.pojos.Embedding;

/**
 * Represents an intermediate result for the expand operator
 *
 * The base part (f0) is the immutable input generated by previous operations
 * The path (f1) is the path we grow while expanding
 * The end node (f2) is the last element in expanded path (a node). It is stored separately
 * to enable using it as a join column
 */
public class ExpandEmbedding extends Tuple3<Embedding, GradoopId[], GradoopId> {

  /**
   * Create a new ExpandIntermediateResult
   */
  public ExpandEmbedding() {
    super();
  }

  /**
   * Create a new expand intermediate result
   * @param base the base part
   * @param path the path along we expanded
   */
  public ExpandEmbedding(Embedding base, GradoopId... path) {
    super(base, ArrayUtils.subarray(path, 0, path.length - 1), path[path.length - 1]);

  }

  /**
   * Returns the base part
   * @return the base part
   */
  public Embedding getBase() {
    return f0;
  }

  /**
   * Returns the path
   * @return the path
   */
  public GradoopId[] getPath() {
    return f1;
  }

  /**
   * Returns the end element
   * @return the end element
   */
  public GradoopId getEnd() {
    return f2;
  }

  /**
   * Expands a previous intermediate result by the given edge
   * (base,(a,b,c),d) x (d,e,f) -> (base, (a,b,c,d,e), f)
   *
   * @param edge the edge along which we expand
   * @return new expanded intermediate result
   */
  public ExpandEmbedding grow(EdgeWithTiePoint edge) {
    return new ExpandEmbedding(
      f0,
      ArrayUtils.addAll(f1, f2, edge.f1, edge.f2)
    );
  }

  /**
   * Size of the path
   * @return path size
   */
  public int pathSize() {
    return f1.length;
  }

  /**
   * Turns the intermediate result into an embedding
   * (base,(a,b,c),d) -> (base, IdListEntry(a,b,c), IdEntry(d))
   *
   * @return embedding representation of the expand intermediate result
   */
  public Embedding toEmbedding() {
    Embedding embedding = getBase();
    embedding.add(f1);
    embedding.add(f2);

    return getBase();
  }
}
