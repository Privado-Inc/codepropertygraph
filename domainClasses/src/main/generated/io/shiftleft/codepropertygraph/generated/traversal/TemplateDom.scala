package io.shiftleft.codepropertygraph.generated.traversal

import overflowdb.traversal._
import io.shiftleft.codepropertygraph.generated.nodes._

/** Traversal steps for TemplateDom */
class TemplateDomTraversalExtGen[NodeType <: TemplateDom](val traversal: Iterator[NodeType]) extends AnyVal {

  /** Traverse to argumentIndex property */
  def argumentIndex: Iterator[scala.Int] =
    traversal.map(_.argumentIndex)

  /** Traverse to nodes where the argumentIndex equals the given `value`
    */
  def argumentIndex(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.argumentIndex == value }

  /** Traverse to nodes where the argumentIndex equals at least one of the given `values`
    */
  def argumentIndex(values: scala.Int*): Iterator[NodeType] = {
    val vset = values.toSet
    traversal.filter { node => vset.contains(node.argumentIndex) }
  }

  /** Traverse to nodes where the argumentIndex is greater than the given `value`
    */
  def argumentIndexGt(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.argumentIndex > value }

  /** Traverse to nodes where the argumentIndex is greater than or equal the given `value`
    */
  def argumentIndexGte(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.argumentIndex >= value }

  /** Traverse to nodes where the argumentIndex is less than the given `value`
    */
  def argumentIndexLt(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.argumentIndex < value }

  /** Traverse to nodes where the argumentIndex is less than or equal the given `value`
    */
  def argumentIndexLte(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.argumentIndex <= value }

  /** Traverse to nodes where argumentIndex is not equal to the given `value`.
    */
  def argumentIndexNot(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.argumentIndex != value }

  /** Traverse to nodes where argumentIndex is not equal to any of the given `values`.
    */
  def argumentIndexNot(values: scala.Int*): Iterator[NodeType] = {
    val vset = values.toSet
    traversal.filter { node => !vset.contains(node.argumentIndex) }
  }

  /** Traverse to argumentName property */
  def argumentName: Iterator[String] =
    traversal.flatMap(_.argumentName)

  /** Traverse to nodes where the argumentName matches the regular expression `value`
    */
  def argumentName(pattern: String): Iterator[NodeType] = {
    if (!Misc.isRegex(pattern)) {
      traversal.filter { node => node.argumentName.isDefined && node.argumentName.get == pattern }
    } else {
      overflowdb.traversal.filter.StringPropertyFilter
        .regexp(traversal.filter(_.argumentName.isDefined))(_.argumentName.get, pattern)
    }
  }

  /** Traverse to nodes where the argumentName matches at least one of the regular expressions in `values`
    */
  def argumentName(patterns: String*): Iterator[NodeType] = {
    overflowdb.traversal.filter.StringPropertyFilter
      .regexpMultiple(traversal.filter(_.argumentName.isDefined))(_.argumentName.get, patterns)
  }

  /** Traverse to nodes where argumentName matches `value` exactly.
    */
  def argumentNameExact(value: String): Iterator[NodeType] =
    traversal.filter { node => node.argumentName.contains(value) }

  /** Traverse to nodes where argumentName matches one of the elements in `values` exactly.
    */
  def argumentNameExact(values: String*): Iterator[NodeType] = {
    if (values.size == 1)
      argumentNameExact(values.head)
    else
      overflowdb.traversal.filter.StringPropertyFilter
        .exactMultiple[NodeType, String](traversal, _.argumentName, values, "ARGUMENT_NAME")
  }

  /** Traverse to nodes where argumentName does not match the regular expression `value`.
    */
  def argumentNameNot(pattern: String): Iterator[NodeType] = {
    if (!Misc.isRegex(pattern)) {
      traversal.filter { node => node.argumentName.isEmpty || node.argumentName.get != pattern }
    } else {
      overflowdb.traversal.filter.StringPropertyFilter
        .regexpNot(traversal.filter(_.argumentName.isDefined))(_.argumentName.get, pattern)
    }
  }

  /** Traverse to nodes where argumentName does not match any of the regular expressions in `values`.
    */
  def argumentNameNot(patterns: String*): Iterator[NodeType] = {
    overflowdb.traversal.filter.StringPropertyFilter
      .regexpNotMultiple(traversal.filter(_.argumentName.isDefined))(_.argumentName.get, patterns)
  }

  /** Traverse to code property */
  def code: Iterator[String] =
    traversal.map(_.code)

  /** Traverse to nodes where the code matches the regular expression `value`
    */
  def code(pattern: String): Iterator[NodeType] = {
    if (!Misc.isRegex(pattern)) {
      codeExact(pattern)
    } else {
      overflowdb.traversal.filter.StringPropertyFilter.regexp(traversal)(_.code, pattern)
    }
  }

  /** Traverse to nodes where the code matches at least one of the regular expressions in `values`
    */
  def code(patterns: String*): Iterator[NodeType] =
    overflowdb.traversal.filter.StringPropertyFilter.regexpMultiple(traversal)(_.code, patterns)

  /** Traverse to nodes where code matches `value` exactly.
    */
  def codeExact(value: String): Iterator[NodeType] = {
    val fastResult = traversal match {
      case init: overflowdb.traversal.InitialTraversal[NodeType] => init.getByIndex("CODE", value).getOrElse(null)
      case _                                                     => null
    }
    if (fastResult != null) fastResult
    else traversal.filter { node => node.code == value }
  }

  /** Traverse to nodes where code matches one of the elements in `values` exactly.
    */
  def codeExact(values: String*): Iterator[NodeType] = {
    if (values.size == 1)
      codeExact(values.head)
    else
      overflowdb.traversal.filter.StringPropertyFilter
        .exactMultiple[NodeType, String](traversal, node => Some(node.code), values, "CODE")
  }

  /** Traverse to nodes where code does not match the regular expression `value`.
    */
  def codeNot(pattern: String): Iterator[NodeType] = {
    if (!Misc.isRegex(pattern)) {
      traversal.filter { node => node.code != pattern }
    } else {
      overflowdb.traversal.filter.StringPropertyFilter.regexpNot(traversal)(_.code, pattern)
    }
  }

  /** Traverse to nodes where code does not match any of the regular expressions in `values`.
    */
  def codeNot(patterns: String*): Iterator[NodeType] = {
    overflowdb.traversal.filter.StringPropertyFilter.regexpNotMultiple(traversal)(_.code, patterns)
  }

  /** Traverse to columnNumber property */
  def columnNumber: Iterator[scala.Int] =
    traversal.flatMap(_.columnNumber)

  /** Traverse to nodes where the columnNumber equals the given `value`
    */
  def columnNumber(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.columnNumber.isDefined && node.columnNumber.get == value }

  /** Traverse to nodes where the columnNumber equals at least one of the given `values`
    */
  def columnNumber(values: scala.Int*): Iterator[NodeType] = {
    val vset = values.toSet
    traversal.filter { node => node.columnNumber.isDefined && vset.contains(node.columnNumber.get) }
  }

  /** Traverse to nodes where the columnNumber is greater than the given `value`
    */
  def columnNumberGt(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.columnNumber.isDefined && node.columnNumber.get > value }

  /** Traverse to nodes where the columnNumber is greater than or equal the given `value`
    */
  def columnNumberGte(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.columnNumber.isDefined && node.columnNumber.get >= value }

  /** Traverse to nodes where the columnNumber is less than the given `value`
    */
  def columnNumberLt(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.columnNumber.isDefined && node.columnNumber.get < value }

  /** Traverse to nodes where the columnNumber is less than or equal the given `value`
    */
  def columnNumberLte(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.columnNumber.isDefined && node.columnNumber.get <= value }

  /** Traverse to nodes where columnNumber is not equal to the given `value`.
    */
  def columnNumberNot(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => !node.columnNumber.isDefined || node.columnNumber.get != value }

  /** Traverse to nodes where columnNumber is not equal to any of the given `values`.
    */
  def columnNumberNot(values: scala.Int*): Iterator[NodeType] = {
    val vset = values.toSet
    traversal.filter { node => !node.columnNumber.isDefined || !vset.contains(node.columnNumber.get) }
  }

  /** Traverse to lineNumber property */
  def lineNumber: Iterator[scala.Int] =
    traversal.flatMap(_.lineNumber)

  /** Traverse to nodes where the lineNumber equals the given `value`
    */
  def lineNumber(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.lineNumber.isDefined && node.lineNumber.get == value }

  /** Traverse to nodes where the lineNumber equals at least one of the given `values`
    */
  def lineNumber(values: scala.Int*): Iterator[NodeType] = {
    val vset = values.toSet
    traversal.filter { node => node.lineNumber.isDefined && vset.contains(node.lineNumber.get) }
  }

  /** Traverse to nodes where the lineNumber is greater than the given `value`
    */
  def lineNumberGt(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.lineNumber.isDefined && node.lineNumber.get > value }

  /** Traverse to nodes where the lineNumber is greater than or equal the given `value`
    */
  def lineNumberGte(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.lineNumber.isDefined && node.lineNumber.get >= value }

  /** Traverse to nodes where the lineNumber is less than the given `value`
    */
  def lineNumberLt(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.lineNumber.isDefined && node.lineNumber.get < value }

  /** Traverse to nodes where the lineNumber is less than or equal the given `value`
    */
  def lineNumberLte(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => node.lineNumber.isDefined && node.lineNumber.get <= value }

  /** Traverse to nodes where lineNumber is not equal to the given `value`.
    */
  def lineNumberNot(value: scala.Int): Iterator[NodeType] =
    traversal.filter { node => !node.lineNumber.isDefined || node.lineNumber.get != value }

  /** Traverse to nodes where lineNumber is not equal to any of the given `values`.
    */
  def lineNumberNot(values: scala.Int*): Iterator[NodeType] = {
    val vset = values.toSet
    traversal.filter { node => !node.lineNumber.isDefined || !vset.contains(node.lineNumber.get) }
  }

  /** Traverse to name property */
  def name: Iterator[String] =
    traversal.map(_.name)

  /** Traverse to nodes where the name matches the regular expression `value`
    */
  def name(pattern: String): Iterator[NodeType] = {
    if (!Misc.isRegex(pattern)) {
      nameExact(pattern)
    } else {
      overflowdb.traversal.filter.StringPropertyFilter.regexp(traversal)(_.name, pattern)
    }
  }

  /** Traverse to nodes where the name matches at least one of the regular expressions in `values`
    */
  def name(patterns: String*): Iterator[NodeType] =
    overflowdb.traversal.filter.StringPropertyFilter.regexpMultiple(traversal)(_.name, patterns)

  /** Traverse to nodes where name matches `value` exactly.
    */
  def nameExact(value: String): Iterator[NodeType] = {
    val fastResult = traversal match {
      case init: overflowdb.traversal.InitialTraversal[NodeType] => init.getByIndex("NAME", value).getOrElse(null)
      case _                                                     => null
    }
    if (fastResult != null) fastResult
    else traversal.filter { node => node.name == value }
  }

  /** Traverse to nodes where name matches one of the elements in `values` exactly.
    */
  def nameExact(values: String*): Iterator[NodeType] = {
    if (values.size == 1)
      nameExact(values.head)
    else
      overflowdb.traversal.filter.StringPropertyFilter
        .exactMultiple[NodeType, String](traversal, node => Some(node.name), values, "NAME")
  }

  /** Traverse to nodes where name does not match the regular expression `value`.
    */
  def nameNot(pattern: String): Iterator[NodeType] = {
    if (!Misc.isRegex(pattern)) {
      traversal.filter { node => node.name != pattern }
    } else {
      overflowdb.traversal.filter.StringPropertyFilter.regexpNot(traversal)(_.name, pattern)
    }
  }

  /** Traverse to nodes where name does not match any of the regular expressions in `values`.
    */
  def nameNot(patterns: String*): Iterator[NodeType] = {
    overflowdb.traversal.filter.StringPropertyFilter.regexpNotMultiple(traversal)(_.name, patterns)
  }

  /** Traverse to order property */
  def order: Iterator[scala.Int] =
    traversal.map(_.order)

  /** Traverse to nodes where the order equals the given `value`
    */
  def order(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.order == value }

  /** Traverse to nodes where the order equals at least one of the given `values`
    */
  def order(values: scala.Int*): Iterator[NodeType] = {
    val vset = values.toSet
    traversal.filter { node => vset.contains(node.order) }
  }

  /** Traverse to nodes where the order is greater than the given `value`
    */
  def orderGt(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.order > value }

  /** Traverse to nodes where the order is greater than or equal the given `value`
    */
  def orderGte(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.order >= value }

  /** Traverse to nodes where the order is less than the given `value`
    */
  def orderLt(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.order < value }

  /** Traverse to nodes where the order is less than or equal the given `value`
    */
  def orderLte(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.order <= value }

  /** Traverse to nodes where order is not equal to the given `value`.
    */
  def orderNot(value: scala.Int): Iterator[NodeType] =
    traversal.filter { _.order != value }

  /** Traverse to nodes where order is not equal to any of the given `values`.
    */
  def orderNot(values: scala.Int*): Iterator[NodeType] = {
    val vset = values.toSet
    traversal.filter { node => !vset.contains(node.order) }
  }

}
