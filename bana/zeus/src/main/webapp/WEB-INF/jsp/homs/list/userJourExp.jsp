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
        <Created>2015-07-21T00:58:50Z</Created>
        <Version>16.00</Version>
    </DocumentProperties>
    <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
        <AllowPNG/>
    </OfficeDocumentSettings>
    <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
        <WindowHeight>5724</WindowHeight>
        <WindowWidth>17256</WindowWidth>
        <WindowTopX>0</WindowTopX>
        <WindowTopY>0</WindowTopY>
        <ProtectStructure>False</ProtectStructure>
        <ProtectWindows>False</ProtectWindows>
    </ExcelWorkbook>
    <Styles>
        <Style ss:ID="Default" ss:Name="Normal">
            <Alignment ss:Vertical="Center"/>
            <Borders/>
            <Font ss:FontName="等线" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
            <Interior/>
            <NumberFormat/>
            <Protection/>
        </Style>
    </Styles>
    <Worksheet ss:Name="Sheet1">
        <Table ss:ExpandedColumnCount="13" ss:ExpandedRowCount="${list.totalCount + 1}" x:FullColumns="1"
               x:FullRows="1" ss:DefaultRowHeight="13.8">
            <Column ss:Index="12" ss:Width="70.8"/>
            <Row>
                <Cell><Data ss:Type="String">流水号</Data></Cell>
                <Cell><Data ss:Type="String">用户编号</Data></Cell>
                <Cell><Data ss:Type="String">用户姓名</Data></Cell>
                <Cell><Data ss:Type="String">用户手机号</Data></Cell>
                <Cell><Data ss:Type="String">配资账户号</Data></Cell>
				<Cell><Data ss:Type="String">业务类型</Data></Cell>
				<Cell><Data ss:Type="String">发生前金额(元)</Data></Cell>
                <Cell><Data ss:Type="String">发生金额(元)</Data></Cell>
				<Cell><Data ss:Type="String">发生后金额(元)</Data></Cell>
                <Cell><Data ss:Type="String">发生时间</Data></Cell>
                <Cell><Data ss:Type="String">发生方向</Data></Cell>
                <Cell><Data ss:Type="String">关联流水号</Data></Cell>
                <Cell><Data ss:Type="String">备注</Data></Cell>
            </Row>
            <c:forEach items="${list.getItems()}" var="u">
                <Row>
                    <Cell><Data ss:Type="String">${u.id}</Data></Cell>
					<Cell><Data ss:Type="String">${u.userId}</Data></Cell>
					<Cell><Data ss:Type="String">${u.userName}</Data></Cell>
					<Cell><Data ss:Type="String">${u.mobile}</Data></Cell>
					<Cell><Data ss:Type="String">${u.pzAccountId}</Data></Cell>
					<Cell><Data ss:Type="String">${u.accountBizType}</Data></Cell>
					<Cell><Data ss:Type="String">${u.preAmount / 100}</Data></Cell>
					<Cell><Data ss:Type="String">${u.transAmount / 100}</Data></Cell>
					<Cell><Data ss:Type="String">${u.postAmount / 100}</Data></Cell>
					<Cell><Data ss:Type="String"><fmt:formatDate value="${u.transDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></Data></Cell>
					<Cell><Data ss:Type="String"><c:if test="${u.seqFlag == 1}">减少</c:if><c:if test="${u.seqFlag == 2}">增加</c:if></Data></Cell>
					<Cell><Data ss:Type="String">${u.refSerialNo}</Data></Cell>
					<Cell><Data ss:Type="String">${u.remark}</Data></Cell>
                </Row>
            </c:forEach>
        </Table>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <PageSetup>
                <Header x:Margin="0.3"/>
                <Footer x:Margin="0.3"/>
                <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
            </PageSetup>
            <Print>
                <ValidPrinterInfo/>
                <PaperSizeIndex>9</PaperSizeIndex>
                <HorizontalResolution>600</HorizontalResolution>
                <VerticalResolution>600</VerticalResolution>
            </Print>
            <Selected/>
            <Panes>
                <Pane>
                    <Number>3</Number>
                    <ActiveRow>3</ActiveRow>
                    <ActiveCol>14</ActiveCol>
                </Pane>
            </Panes>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
</Workbook>