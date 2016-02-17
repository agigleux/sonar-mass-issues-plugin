/*
 * SonarQube mass-issues Plugin
 * Copyright (C) 2014-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.massissues;

import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.batch.sensor.issue.internal.DefaultIssueLocation;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

@DependsUpon()
public class MassIssuesSensor implements Sensor {

  public static final RuleKey RULE_KEY = RuleKey.of(MassIssuesRules.REPOSITORY_KEY, "OneIssuePerLine");

  private static final Logger LOGGER = Loggers.get(MassIssuesSensor.class);

  private final FileSystem fs;

  /**
   * Use of IoC to get FileSystem
   */
  public MassIssuesSensor(FileSystem fs) {
    this.fs = fs;
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    return fs.hasFiles(fs.predicates().hasLanguage("java"));
  }

  @Override
  public void analyse(Project project, SensorContext sensorContext) {

    for (InputFile inputFile : fs.inputFiles(fs.predicates().hasLanguage("java"))) {
      Measure linesMeasure = sensorContext.getMeasure(CoreMetrics.LINES);
      if (linesMeasure == null) {
        LOGGER.warn("Missing measure " + CoreMetrics.LINES_KEY + " on " + inputFile.absolutePath());
      } else {
        for (int line = 1; line <= linesMeasure.getValue().intValue(); line++) {
          NewIssueLocation location = new DefaultIssueLocation();
          location.on(inputFile);
          location.at(inputFile.newRange(line, 0, line, 1));
          location.message("Mass Issue Plugin");

          sensorContext.newIssue().at(location).forRule(RULE_KEY).save();
        }
      }
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
