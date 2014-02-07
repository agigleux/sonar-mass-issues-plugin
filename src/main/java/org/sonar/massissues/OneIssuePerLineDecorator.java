/*
 * SonarQube mass-issues Plugin
 * Copyright (C) 2014 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.massissues;

import org.slf4j.LoggerFactory;
import org.sonar.api.batch.*;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.ResourceUtils;
import org.sonar.api.rule.RuleKey;

@DependedUpon(DecoratorBarriers.ISSUES_ADDED)
public class OneIssuePerLineDecorator implements Decorator {

  public static final RuleKey RULE_KEY = RuleKey.of("massissues", "OneIssuePerLine");

  private final ResourcePerspectives perspectives;

  public OneIssuePerLineDecorator(ResourcePerspectives perspectives) {
    this.perspectives = perspectives;
  }

  @DependsUpon
  public Metric dependsUponLinesMeasure() {
    // UGLY - this method is marked as unused by IDE
    return CoreMetrics.LINES;
  }

  @Override
  public void decorate(Resource resource, DecoratorContext decoratorContext) {
    Issuable issuable = perspectives.as(Issuable.class, resource);
    if (issuable != null && ResourceUtils.isFile(resource)) {
      Measure linesMeasure = decoratorContext.getMeasure(CoreMetrics.LINES);
      if (linesMeasure == null) {
        LoggerFactory.getLogger(getClass()).warn("Missing measure " + CoreMetrics.LINES_KEY + " on " + issuable.component());
      } else {
        for (int line = 1; line <= linesMeasure.getValue().intValue(); line++) {
          issuable.addIssue(issuable.newIssueBuilder()
            .line(line)
            .ruleKey(RULE_KEY)
            .message("This issue is generated on each line")
            .build());
        }
      }
    }
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    return true;
  }
}
