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

import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;

import java.util.Arrays;
import java.util.List;

public class MassIssuesRules extends RuleRepository {

  public MassIssuesRules() {
    // UGLY - repository key is unique among all languages. So why constructor requires language ?
    super("massissues", "java");
    setName("Mass Issues");
  }

  @Override
  public List<Rule> createRules() {
    return Arrays.asList(
      Rule.create(OneIssuePerLineDecorator.RULE_KEY.repository(), OneIssuePerLineDecorator.RULE_KEY.rule()).setName("One issue per line").setDescription("Generates one issue per line !")
    );
  }
}
