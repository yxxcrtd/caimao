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
        <Table ss:ExpandedColumnCount="21" ss:ExpandedRowCount="${list.totalCount + 1}" x:FullColumns="1"
               x:FullRows="1" ss:DefaultRowHeight="13.8">
            <Column ss:Index="12" ss:Width="70.8"/>
            <Row>
                <Cell><Data ss:Type="String">合约编号</Data></Cell>
                <Cell><Data ss:Type="String">合约类型</Data></Cell>
                <Cell><Data ss:Type="String">用户编号</Data></Cell>
                <Cell><Data ss:Type="String">用户姓名</Data></Cell>
                <Cell><Data ss:Type="String">产品</Data></Cell>
                <Cell><Data ss:Type="String">合约开始日期</Data></Cell>
                <Cell><Data ss:Type="String">合约结束日期</Data></Cell>
                <Cell><Data ss:Type="String">起息日</Data></Cell>
                <Cell><Data ss:Type="String">保证金(元)</Data></Cell>
                <Cell><Data ss:Type="String">借款金额(元)</Data></Cell>
                <Cell><Data ss:Type="String">利率</Data></Cell>
                <Cell><Data ss:Type="String">计息方式</Data></Cell>
                <Cell><Data ss:Type="String">结息方式</Data></Cell>
                <Cell><Data ss:Type="String">结息周期天数</Data></Cell>
                <Cell><Data ss:Type="String">产品期限天数</Data></Cell>
                <Cell><Data ss:Type="String">是否还款</Data></Cell>
                <Cell><Data ss:Type="String">还款金额</Data></Cell>
                <Cell><Data ss:Type="String">还款时间</Data></Cell>
                <Cell><Data ss:Type="String">应收金额</Data></Cell>
                <Cell><Data ss:Type="String">已收金额</Data></Cell>
                <Cell><Data ss:Type="String">未收金额</Data></Cell>
            </Row>
            <c:forEach items="${list.getItems()}" var="u">
                <Row>
                    <Cell><Data ss:Type="String">${u.contractNo}</Data></Cell>
                    <Cell><Data ss:Type="String"><c:choose><c:when test="${u.contractType==0}">借款</c:when><c:when test="${u.contractType==1}">追加</c:when></c:choose></Data></Cell>
                    <Cell><Data ss:Type="String">${u.userId}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.userRealName}</Data></Cell>
                    <Cell><Data ss:Type="String"><c:choose><c:when test="${u.prodId==1}">免费体验</c:when><c:when test="${u.prodId==3}">按月配</c:when><c:when test="${u.prodId==4}">按日配</c:when><c:when test="${u.prodId==800461611335681}">按月配(免息)</c:when><c:when test="${u.prodId==800461779107841}">按月</c:when><c:when test="${u.prodId==800461779107843}">按月配(低息)</c:when><c:when test="${u.prodId==800461812662273}">按日配(免息)</c:when><c:when test="${u.prodId==800461812662274}">按日配(免息1)</c:when><c:when test="${u.prodId==801527635640321}">按月保留</c:when><c:when test="${u.prodId==802234593968142}">按月配(借贷)</c:when><c:otherwise>${u.prodId}</c:otherwise></c:choose></Data></Cell>
                    <Cell><Data ss:Type="String">${u.contractBeginDate}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.contractEndDate}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.beginInterestDate}</Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.cashAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.loanAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String">${u.interestRate}</Data></Cell>
                    <Cell><Data ss:Type="String"><c:choose><c:when test="${u.interestAccrualMode==0}">按自然日计息</c:when><c:when test="${u.interestAccrualMode==1}">按交易日计息</c:when><c:when test="${u.interestAccrualMode==2}">按月计息</c:when></c:choose></Data></Cell>
                    <Cell><Data ss:Type="String"><c:choose><c:when test="${u.interestSettleMode==0}">先结</c:when><c:when test="${u.interestSettleMode==1}">后结</c:when></c:choose></Data></Cell>
                    <Cell><Data ss:Type="String">${u.interestSettleDays}</Data></Cell>
                    <Cell><Data ss:Type="String">${u.prodTerm}</Data></Cell>
                    <Cell><Data ss:Type="String"><c:choose><c:when test="${u.contractStatus==0}">未还款</c:when><c:when test="${u.contractStatus==1}">已还款</c:when></c:choose></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.repayAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatDate value="${u.repayDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.interestShould/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.interestAlready/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${(u.interestShould - u.interestAlready)/100}" type="number"/></Data></Cell>
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