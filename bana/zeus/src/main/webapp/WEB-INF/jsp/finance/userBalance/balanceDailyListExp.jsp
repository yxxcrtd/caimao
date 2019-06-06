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
        <Table ss:ExpandedColumnCount="16" ss:ExpandedRowCount="${list.totalCount + 1}" x:FullColumns="1"
               x:FullRows="1" ss:DefaultColumnWidth="57.6"
               ss:DefaultRowHeight="15.600000000000001">
            <Column ss:Width="64.2"/>
            <Row>
                <Cell><Data ss:Type="String">日期</Data></Cell>
                <Cell><Data ss:Type="String">可用资产</Data></Cell>
                <Cell><Data ss:Type="String">冻结资产</Data></Cell>
                <Cell><Data ss:Type="String">用户数量</Data></Cell>
                <Cell><Data ss:Type="String">累计利息</Data></Cell>
                <Cell><Data ss:Type="String">累计借贷金额</Data></Cell>
                <Cell><Data ss:Type="String">累计还款金额</Data></Cell>
                <Cell><Data ss:Type="String">累计P2P利息金额</Data></Cell>
                <Cell><Data ss:Type="String">累计P2P利息发放金额</Data></Cell>
                <Cell><Data ss:Type="String">累计P2P投资金额</Data></Cell>
                <Cell><Data ss:Type="String">累计P2P投资流标金额</Data></Cell>
                <Cell><Data ss:Type="String">累计P2P投资满标金额</Data></Cell>
                <Cell><Data ss:Type="String">累计P2P投资已还金额</Data></Cell>
                <Cell><Data ss:Type="String">累计充值金额</Data></Cell>
                <Cell><Data ss:Type="String">累计提现金额</Data></Cell>
                <Cell><Data ss:Type="String">当前借款金额</Data></Cell>
            </Row>
            <c:forEach items="${list.getItems()}" var="u">
                <Row>
                    <Cell><Data ss:Type="String"><fmt:formatDate value="${u.created}" pattern="yyyy-MM-dd"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.availableAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.freezeAmount/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String">${u.userCount}</Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.loanInterestTotal/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.loanTotal/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.loanTotalRepay/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.p2pInterestTotal/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.p2pInterestTotalPay/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.p2pInvestTotal/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.p2pInvestTotalFail/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.p2pInvestTotalSuccess/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.p2pInvestTotalRepay/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.depositTotal/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.withdrawTotal/100}" type="number"/></Data></Cell>
                    <Cell><Data ss:Type="String"><fmt:formatNumber value="${u.loanBalance/100}" type="number"/></Data></Cell>
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