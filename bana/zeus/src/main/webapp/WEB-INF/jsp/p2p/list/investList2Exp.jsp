<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%><?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:o="urn:schemas-microsoft-com:office:office"
          xmlns:x="urn:schemas-microsoft-com:office:excel"
          xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:html="http://www.w3.org/TR/REC-html40">
    <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
        <Author>huobi</Author>
        <LastAuthor>huobi</LastAuthor>
        <Created>2015-07-28T02:39:29Z</Created>
        <Version>16.00</Version>
    </DocumentProperties>
    <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
        <AllowPNG/>
    </OfficeDocumentSettings>
    <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
        <WindowHeight>9144</WindowHeight>
        <WindowWidth>23040</WindowWidth>
        <WindowTopX>0</WindowTopX>
        <WindowTopY>0</WindowTopY>
        <ProtectStructure>False</ProtectStructure>
        <ProtectWindows>False</ProtectWindows>
    </ExcelWorkbook>
    <Styles>
        <Style ss:ID="Default" ss:Name="Normal">
            <Alignment ss:Vertical="Center"/>
            <Borders/>
            <Font ss:FontName="微软雅黑" x:CharSet="134" x:Family="Swiss" ss:Size="11"
            ss:Color="#000000"/>
            <Interior/>
            <NumberFormat/>
            <Protection/>
        </Style>
        <Style ss:ID="s62">
            <NumberFormat ss:Format="Short Date"/>
        </Style>
    </Styles>
    <Worksheet ss:Name="Sheet1">
        <Table ss:ExpandedColumnCount="10" ss:ExpandedRowCount="${req.totalCount + 1}" x:FullColumns="1"
               x:FullRows="1" ss:DefaultColumnWidth="57.6"
               ss:DefaultRowHeight="15.600000000000001">
            <Column ss:Width="64.2"/>
            <Row>
                <Cell><Data ss:Type="String">投资编号</Data></Cell>
                <Cell><Data ss:Type="String">姓名</Data></Cell>
                <Cell><Data ss:Type="String">手机</Data></Cell>
                <Cell><Data ss:Type="String">标的名称</Data></Cell>
                <Cell><Data ss:Type="String">标的期限</Data></Cell>
                <Cell><Data ss:Type="String">投资金额</Data></Cell>
                <Cell><Data ss:Type="String">年利息</Data></Cell>
                <Cell><Data ss:Type="String">已收利息</Data></Cell>
                <Cell><Data ss:Type="String">投资时间</Data></Cell>
                <Cell><Data ss:Type="String">计息时间</Data></Cell>
            </Row>
            <c:forEach items="${req.getItems()}" var="u">
                <Row>
                    <Cell><Data ss:Type="String">${u.investId}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.userName}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.mobile}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.targetName} -- (编号: ${u.targetId})</Data></Cell>
                    <Cell><Data ss:Type="String">${u.liftTime}</Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.investValue/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber type="number" value="${u.yearRate*100}" pattern="#.##"/>%</Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.payInterest/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatDate value="${u.investCreated}" type="both" dateStyle="default" /></Data></Cell>
                    <Cell><Data ss:Type="String"><c:if test="${not empty u.interestDatetime}"><fmt:formatDate value="${u.interestDatetime}" type="both" dateStyle="default" /></c:if></Data></Cell>
                </Row>
            </c:forEach>
        </Table>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <PageSetup>
                <Header x:Margin="0.3"/>
                <Footer x:Margin="0.3"/>
                <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
            </PageSetup>
            <Selected/>
            <Panes>
                <Pane>
                    <Number>3</Number>
                    <ActiveRow>2</ActiveRow>
                    <ActiveCol>3</ActiveCol>
                </Pane>
            </Panes>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
</Workbook>