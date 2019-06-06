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
        <Table ss:ExpandedColumnCount="25" ss:ExpandedRowCount="${list.totalCount + 1}" x:FullColumns="1"
               x:FullRows="1" ss:DefaultRowHeight="13.8">
            <Column ss:Index="12" ss:Width="70.8"/>
            <Row>
                <Cell><Data ss:Type="String">发生日期</Data></Cell>
                <Cell><Data ss:Type="String">流水序号</Data></Cell>
                <Cell><Data ss:Type="String">发生业务</Data></Cell>
                <Cell><Data ss:Type="String">证券代码</Data></Cell>
                <Cell><Data ss:Type="String">证券名称</Data></Cell>
                <Cell><Data ss:Type="String">账户</Data></Cell>
                <Cell><Data ss:Type="String">单元编号</Data></Cell>
                <Cell><Data ss:Type="String">单元名称</Data></Cell>
                <Cell><Data ss:Type="String">发生金额</Data></Cell>
                <Cell><Data ss:Type="String">发生后余额</Data></Cell>
                <Cell><Data ss:Type="String">委托方向</Data></Cell>
                <Cell><Data ss:Type="String">委托价格</Data></Cell>
                <Cell><Data ss:Type="String">发生数量</Data></Cell>
                <Cell><Data ss:Type="String">交易费</Data></Cell>
                <Cell><Data ss:Type="String">印花税</Data></Cell>
                <Cell><Data ss:Type="String">过户费</Data></Cell>
                <Cell><Data ss:Type="String">佣金</Data></Cell>
                <Cell><Data ss:Type="String">经手费</Data></Cell>
                <Cell><Data ss:Type="String">证管费</Data></Cell>
                <Cell><Data ss:Type="String">币种</Data></Cell>
                <Cell><Data ss:Type="String">发生科目</Data></Cell>
                <Cell><Data ss:Type="String">科目发生额</Data></Cell>
                <Cell><Data ss:Type="String">科目发生后余额</Data></Cell>
                <Cell><Data ss:Type="String">备注</Data></Cell>
                <Cell><Data ss:Type="String">技术服务费</Data></Cell>
            </Row>
            <c:forEach items="${list.getItems()}" var="u">
                <Row>
                    <Cell><Data ss:Type="String"><fmt:formatDate value="${u.transDate}" pattern="yyyy-MM-dd"/></Data></Cell>
                    <Cell><Data ss:Type="String">${u.transNo}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.transBizType}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.stockCode}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.stockName}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.account}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.accountUnitNo}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.accountUnitName}</Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.transAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.postAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String">${u.entrustDirection}</Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.entrustPrice/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.entrustAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.tradeFee/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.stampDuty/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.transferFee/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.commission/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.handlingFee/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.secCharges/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String">${u.currency}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.transSubject}</Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.subjectTransAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.subjectPostAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String">${u.remark}</Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.technicalServices/100}" type="number"/></Data></Cell>
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