/**
 * (c) 2003-2024 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.mulechain.internal.tools;

import dev.langchain4j.agent.tool.Tool;
import org.mule.extension.mulechain.internal.util.ExcludeFromGeneratedCoverage;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@ExcludeFromGeneratedCoverage
public class DynamicToolWrapper implements Tool {

  private final String name;
  private final String description;

  public DynamicToolWrapper(String name, String description) {
    this.name = name;
    this.description = description;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String[] value() {
    return new String[] {description};
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return Tool.class;
  }

  public static Tool create(String name, String description) {
    return (Tool) Proxy.newProxyInstance(
                                         Tool.class.getClassLoader(),
                                         new Class[] {Tool.class},
                                         new DynamicToolInvocationHandler(name, description));
  }

  private static class DynamicToolInvocationHandler implements InvocationHandler {

    private final String name;
    private final String description;

    public DynamicToolInvocationHandler(String name, String description) {
      this.name = name;
      this.description = description;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      switch (method.getName()) {
        case "name":
          return name;
        case "value":
          return new String[] {description};
        case "annotationType":
          return Tool.class;
        default:
          throw new UnsupportedOperationException("Method not implemented: " + method.getName());
      }
    }
  }
}
