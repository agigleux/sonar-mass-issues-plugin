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

public class MassIssuesRules implements org.sonar.api.server.rule.RulesDefinition {

  public static final String REPOSITORY_KEY = "massissues";

  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(REPOSITORY_KEY, "java").setName("Mass Issues");

    NewRule oneIssueByLineRule = repository.createRule("oneIssueByLine")
      .setName("One issue per line")
      .setHtmlDescription("Generates one issue per line !");

    oneIssueByLineRule
      .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
      .setDebtRemediationFunction(oneIssueByLineRule.debtRemediationFunctions().linearWithOffset("1h", "30min"));

    // don't forget to call done() to finalize the definition
    repository.done();
  }
}
