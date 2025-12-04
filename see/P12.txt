//Code: Session1.jsp (Input Page)
<%@ page language="java" %>
<html>
<body>
<h2>Enter Your Name</h2>
<form action="Session2.jsp" method="get">
Name: <input type="text" name="uname" required>
<br><br>
<input type="submit" value="Submit">
</form>
</body>
</html>

//Code: Session2.jsp (Welcome and Session Start)
<%@ page language="java" import="java.util.*" %>
<%
String name = request.getParameter("uname");
// Save name and start time into the session
if (name != null) {
session.setAttribute("user", name);
session.setAttribute("startTime", new Date().getTime());
}
name = (String) session.getAttribute("user");
long start = (Long) session.getAttribute("startTime");
%>
<html>
<body>
<p align="right">
Start Time: <%= new Date(start).toString() %>
</p>
<h2>Hello <%= name %>!</h2>
<form action="Logout.jsp" method="get">
<input type="submit" value="Logout">
</form>
</body>
</html>

//Code: Logout.jsp (Session End and Duration)
<%@ page language="java" import="java.util.*" %>
<%
String name = (String) session.getAttribute("user");
long start = (Long) session.getAttribute("startTime");
long end = new Date().getTime();
long duration = end - start;
long seconds = duration / 1000;
long minutes = duration / (1000 * 60);
long hours = duration / (1000 * 60 * 60);
session.invalidate();
%>
<html>
<body>
<h2>Thank You <%= name %>!</h2>
Session Duration:
<br>
<%= hours %> hours,
<%= minutes %> minutes,
<%= seconds %> seconds
</body>
</html>
