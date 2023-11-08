import java.io.IOException;
import java.sql.ResultSet;
import java.util.Stack;
public class Util {

  public static void HandleText(String message, Client client) {

    if (message.equals("get text"))
    {
      ResultSet rs = ServerManager.db.executeQuery("SELECT * FROM `text`");
      try {
        while (rs.next()) {
          client.getWriter().println(rs.getString("text"));
        }
        return;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (isValidExpression(message)) {
      String postfix = infixToPostfix(message);
      System.out.println("Postfix: " + postfix);
      client.getWriter().println(client.getName() + ": " + message);
      client.getWriter().println("Server: " + String.valueOf(evaluatePostfix(postfix)));
    } else {
      client.getWriter().println(client.getName() + ": " + message);
      client.getWriter().println("Server: " + ReverseText(message));
      client.getWriter().println("Server: " + UpperCaseText(message));
      client.getWriter().println("Server: " + LowerCase(message));
      client.getWriter().println("Server: " + UpperToLowerAndLowerToUpper(message));
      client.getWriter().println("Server: " + CountText(message));
    }
  }

  public static void HandleTextUser(String message, Client client)
  {
    for (Client c : ServerManager.clients) {
      if (c.getChatType() == 1)
      {
        c.getWriter().println(client.getName() + ": " + message);
      }
    }
  }

  public static boolean isValidExpression(String expr) {
    Stack<Character> stack = new Stack<>();
    boolean hasPreviousOperator = true; // Để xử lý trường hợp số âm ở đầu biểu thức

    for (int i = 0; i < expr.length(); i++) {
      char ch = expr.charAt(i);

      if (Character.isWhitespace(ch)) {
        continue;  // Bỏ qua các ký tự khoảng trắng
      } else if (Character.isDigit(ch)) {
        hasPreviousOperator = false;
        while (i + 1 < expr.length() && Character.isDigit(expr.charAt(i + 1))) {
          i++; // Đi qua số có nhiều chữ số
        }
      } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
        if (hasPreviousOperator && ch != '-') {
          return false; // Không cho phép hai toán tử liên tiếp (trừ trường hợp số âm)
        }
        hasPreviousOperator = true;
      } else if (ch == '(') {
        stack.push(ch);
        hasPreviousOperator = true;
      } else if (ch == ')') {
        if (stack.isEmpty() || stack.pop() != '(') {
          return false;
        }
        hasPreviousOperator = false;
      } else {
        return false; // Ký tự không hợp lệ
      }
    }

    return stack.isEmpty();
  }

  public static String infixToPostfix(String expr) {
    StringBuilder result = new StringBuilder();
    Stack<Character> stack = new Stack<>();
    for (int i = 0; i < expr.length(); i++) {
      char ch = expr.charAt(i);
      System.out.println(ch);
      if (Character.isDigit(ch)) {
        // Handle multi-digit numbers
        StringBuilder number = new StringBuilder();
        while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
          number.append(expr.charAt(i));
          i++;
        }
        i--;  // Adjust index after the loop
        result.append(number).append(" ");
      } else if (ch == '(') {
        stack.push(ch);
      } else if (ch == ')') {
        while (!stack.isEmpty() && stack.peek() != '(')
          result.append(stack.pop()).append(" ");
        if (!stack.isEmpty()) {
          stack.pop();
        }
      } else if (isOperator(ch)) {
        while (!stack.isEmpty() && (isOperator(stack.peek()) && precedence(ch) <= precedence(stack.peek())))
          result.append(stack.pop()).append(" ");
        stack.push(ch);
      }
    }
    while (!stack.isEmpty()) {
      result.append(stack.pop()).append(" ");
    }
    return result.toString().trim();  // Remove trailing space
  }

  public static boolean isOperator(char ch) {
    return ch == '+' || ch == '-' || ch == '*' || ch == '/';
  }

  public static int precedence(char operator) {
    switch (operator) {
      case '+':
      case '-':
        return 1;
      case '*':
      case '/':
        return 2;
      default:
        throw new IllegalArgumentException("Invalid operator: " + operator);
    }
  }



  public static int evaluatePostfix(String expr) {
    Stack<Integer> stack = new Stack<>();
    String[] tokens = expr.split(" ");  // Split expression by space to get tokens
    for (String token : tokens) {
      if (Character.isDigit(token.charAt(0))) {
        stack.push(Integer.parseInt(token));
      } else {
        int val1 = stack.pop();
        int val2 = stack.pop();
        char operator = token.charAt(0);
        switch (operator) {
          case '+':
            stack.push(val2 + val1);
            break;
          case '-':
            stack.push(val2 - val1);
            break;
          case '*':
            stack.push(val2 * val1);
            break;
          case '/':
            stack.push(val2 / val1);
            break;
        }
      }
    }
    return stack.pop();
  }

  public static String ReverseText(String txt) {
    String result = "";
    for (int i = txt.length() - 1; i >= 0; i--) {
      result += txt.charAt(i);
    }
    return result;
  }

  public static String UpperCaseText(String txt) {
    String result = "";
    for (int i = 0; i < txt.length(); i++) {
      if (txt.charAt(i) >= 'a' && txt.charAt(i) <= 'z') {
        result += (char) (txt.charAt(i) - 32);
      } else {
        result += txt.charAt(i);
      }
    }

    return result;
  }

  public static String LowerCase(String txt) {
    String result = "";
    for (int i = 0; i < txt.length(); i++) {
      if (txt.charAt(i) >= 'A' && txt.charAt(i) <= 'Z') {
        result += (char) (txt.charAt(i) + 32);
      } else {
        result += txt.charAt(i);
      }
    }

    return result;
  }

  public static String UpperToLowerAndLowerToUpper(String txt) {
    String result = "";
    for (int i = 0; i < txt.length(); i++) {
      if (txt.charAt(i) >= 'A' && txt.charAt(i) <= 'Z') {
        result += (char) (txt.charAt(i) + 32);
      } else if (txt.charAt(i) >= 'a' && txt.charAt(i) <= 'z') {
        result += (char) (txt.charAt(i) - 32);
      } else {
        result += txt.charAt(i);
      }
    }

    return result;
  }

  // Đếm số từ và các nguyên âm của chuỗi đã gửi từ client
  public static String CountText(String txt) {
    String result = "Số từ là: ";
    int count = 0;
    int nguyenAm = 0;
    for (int i = 0; i < txt.length(); i++) {
      if (txt.charAt(i) == ' ') {
        count++;
      }

      if (txt.charAt(i) == 'a' || txt.charAt(i) == 'e' || txt.charAt(i) == 'i' || txt.charAt(i) == 'o'
          || txt.charAt(i) == 'u' || txt.charAt(i) == 'A' || txt.charAt(i) == 'E' || txt.charAt(i) == 'I'
          || txt.charAt(i) == 'O' || txt.charAt(i) == 'U') {
        nguyenAm++;
      }
    }

    result += count + " và số nguyên âm là: " + nguyenAm;
    return result;
  }
}


