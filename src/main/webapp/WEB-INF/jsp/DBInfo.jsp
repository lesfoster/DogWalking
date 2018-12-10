<HTML>
<HEAD>
<TITLE>DB Info</TITLE>
</HEAD>
<BODY>

<%@ page import="main.*" %>
<%@ page import="servlet.*" %>

<CENTER>
<H3>DB Info:</H3>
</CENTER>

<PRE>

<% 		
		out.print(Action.outputDBInfo());
%>

</PRE>

<BR>


</BODY>
</HTML>

