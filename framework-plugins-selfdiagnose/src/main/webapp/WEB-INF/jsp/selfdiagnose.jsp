<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<style>
			table {
				border-collapse: collapse;
				width: 100%;
			}

			th {
				background-color: <c:out value="${statusOk ? '#4CAF50' : '#ACE'}"/>;
				color: white;
			}

			tr:nth-child(odd) {
			    background-color: <c:out value="${selfdiagnose.layout.tableColor}"/>
            }

			tr.item-false td {
                background-color: #f99;
            }


			th, td {
                text-align: left;
                padding: 8px;
            }
        </style>
	</head>
	<body>
		<h1>${selfdiagnose.name}</h1>
		<table>
		    <tr>
		        <th style="width: 35%">Key</th>
		        <th>Value</th>
            </tr>
            <c:forEach items="${selfdiagnose.items}" var="item">
                <tr class="item-${item.value.success}">
                    <td>${item.key}</td>
                    <td>${item.value.message}</td>
                </tr>
            </c:forEach>
		</table>

        <br/><br/>
		Version: ${selfdiagnose.version}<br/>
        Time: <fmt:formatDate value="${currentTime.time}" pattern="yyyy-MM-dd HH:mm:ss.SSS" /><br/>
		Status: <c:choose>
                  <c:when test="${statusOk}">All checks passed</c:when>
                  <c:otherwise>There are ${failingChecks} failing checks</c:otherwise>
                </c:choose>
	</body>
</html>