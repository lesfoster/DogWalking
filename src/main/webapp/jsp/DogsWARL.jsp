<HTML>
<HEAD>
<TITLE>WARL Dogs</TITLE>
</HEAD>
<BODY>

<%@ page import="main.*" %>
<%@ page import="servlet.*" %>

<CENTER>
<H3>WARL Dogs:</H3>


<FORM action="/Dogs/Action?OP=FormProc" method="post">


<% 		
		out.print(Dog.outputHTMLdogsTable (Action.getDogs()));
%>

<BR>

</FORM>

</CENTER>
</BODY>
</HTML>

