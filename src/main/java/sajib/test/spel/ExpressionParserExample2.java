package sajib.test.spel;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ExpressionParserExample2 {
	public static void main(String[] args) {
		ExpressionParser expressionParser = new SpelExpressionParser();

		// create EvaluationContext from bean
		SampleBean contextBean = new SampleBean();
		StandardEvaluationContext testContext = new StandardEvaluationContext(contextBean);

		// 9. Property value
		Expression expression = expressionParser.parseExpression("property");
		String strVal = expression.getValue(testContext, String.class);
		System.out.println("9. Property value:\n" + strVal);

		// 10. Compare property
		expression = expressionParser.parseExpression("property == 'String property'");
		boolean boolVal = expression.getValue(testContext, Boolean.class);
		System.out.println("10. Compare property:\n" + boolVal);

		// 11. List value
		expression = expressionParser.parseExpression("arrayList[0]");
		strVal = expression.getValue(testContext, String.class);
		System.out.println("11. List value:\n" + strVal);

		// 12. Map value
		expression = expressionParser.parseExpression("hashMap['key 1']");
		strVal = expression.getValue(testContext, String.class);
		System.out.println("12. Map value:\n" + strVal);
	}
}
