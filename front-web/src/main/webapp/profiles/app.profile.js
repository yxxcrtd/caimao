var profile = (function(){
    return {
        basePath: "../", // help finding packages whose location is a relative path
        releaseDir: "D:/release", // where the release position
        releaseName: "lib",
        action: "release",
        layerOptimize: "closure",
		optimize: "",
		cssOptimize: "comments", // css minimum
		mini: true,
		stripConsole: "all",
		selectorEngine: "acme",
		useSourceMaps: false,

        packages:[{
            name: "dojo",
            location: "dojo"
        },{
            name: "dijit",
            location: "dijit"
        },{
            name: "dojox",
            location: "dojox"
        },{
            name: "app",
            location: "app"
        },{
        	name: "min",
            location: "min"
        }],

        layers: {
            "min/dojo": { // base js
                include: [ "dojo/i18n", "dojo/domReady", "app/main","dojo/has","dojo/dom-geometry","dojo/sniff"],
                customBase: true,
                boot: true
            },
            // subscript per page
            "min/AccountInfo": {include: ["app/controllers/AccountInfo"], includeLocales: ['zh-cn']},
  	        "min/AddContract": {include: ["app/controllers/AddContract"], includeLocales: ['zh-cn']},
  	        "min/ApplyInquiry": {include: ["app/controllers/ApplyInquiry"], includeLocales: ['zh-cn']},
  	        "min/ApplyLoan": {include: ["app/controllers/ApplyLoan"], includeLocales: ['zh-cn']},
  	        "min/Avatar": {include: ["app/controllers/Avatar"], includeLocales: ['zh-cn']},
  	        "min/Bankcard": {include: ["app/controllers/Bankcard"], includeLocales: ['zh-cn']},
  			"min/BankcardBinding": {include: ["app/controllers/BankcardBinding"], includeLocales: ['zh-cn']},
  			"min/Certification": {include: ["app/controllers/Certification"], includeLocales: ['zh-cn']},
  			"min/ChangeLoginPwd": {include: ["app/controllers/ChangeLoginPwd"], includeLocales: ['zh-cn']},
  			"min/ChangeMail": {include: ["app/controllers/ChangeMail"], includeLocales: ['zh-cn']},
  			"min/ChangeMobile": {include: ["app/controllers/ChangeMobile"], includeLocales: ['zh-cn']},
  			"min/ChangeTradePwd": {include: ["app/controllers/ChangeTradePwd"], includeLocales: ['zh-cn']},
  			"min/CompanyCulture": {include: ["app/controllers/CompanyCulture"], includeLocales: ['zh-cn']},
  			"min/CompanyIntro": {include: ["app/controllers/CompanyIntro"], includeLocales: ['zh-cn']},
  			"min/CompanyQual": {include: ["app/controllers/CompanyQual"], includeLocales: ['zh-cn']},
  			"min/ContactUs": {include: ["app/controllers/ContactUs"], includeLocales: ['zh-cn']},
  			"min/Contract": {include: ["app/controllers/Contract"], includeLocales: ['zh-cn']},
  			"min/Contracts": {include: ["app/controllers/Contracts"], includeLocales: ['zh-cn']},
  			"min/DemoHome": {include: ["app/controllers/DemoHome"], includeLocales: ['zh-cn']},
  			"min/DevelopHistory": {include: ["app/controllers/DevelopHistory"], includeLocales: ['zh-cn']},
  			"min/Download": {include: ["app/controllers/Download"], includeLocales: ['zh-cn']},
  			"min/FinancingCheck": {include: ["app/controllers/FinancingCheck"], includeLocales: ['zh-cn']},
  			"min/ForgetLoginPwd": {include: ["app/controllers/ForgetLoginPwd"], includeLocales: ['zh-cn']},
  			"min/Fund": {include: ["app/controllers/Fund"], includeLocales: ['zh-cn']},
  			"min/guide": {include: ["app/controllers/guide"], includeLocales: ['zh-cn']},
  			"min/Help": {include: ["app/controllers/Help"], includeLocales: ['zh-cn']},
  			"min/Helper": {include: ["app/controllers/Helper"], includeLocales: ['zh-cn']},
  			"min/HisContract": {include: ["app/controllers/HisContract"], includeLocales: ['zh-cn']},
  			"min/Home": {include: ["app/controllers/Home"], includeLocales: ['zh-cn']},
  			"min/Home2": {include: ["app/controllers/Home2"], includeLocales: ['zh-cn']},
  			"min/IDX": {include: ["app/controllers/IDX"], includeLocales: ['zh-cn']},
  			"min/Information": {include: ["app/controllers/Information"], includeLocales: ['zh-cn']},
  			"min/IPZ": {include: ["app/controllers/IPZ"], includeLocales: ['zh-cn']},
  			"min/JoinUs": {include: ["app/controllers/JoinUs"], includeLocales: ['zh-cn']},
  			"min/Loan": {include: ["app/controllers/Loan"], includeLocales: ['zh-cn']},
  			"min/LoanBorrow": {include: ["app/controllers/LoanBorrow"], includeLocales: ['zh-cn']},
  			"min/LoanDXB": {include: ["app/controllers/LoanDXB"], includeLocales: ['zh-cn']},
  			"min/LoanExtend": {include: ["app/controllers/LoanExtend"], includeLocales: ['zh-cn']},
  			"min/LoanExtra": {include: ["app/controllers/LoanExtra"], includeLocales: ['zh-cn']},
  			"min/LoanInquiry": {include: ["app/controllers/LoanInquiry"], includeLocales: ['zh-cn']},
  			"min/LoanProtocol": {include: ["app/controllers/LoanProtocol"], includeLocales: ['zh-cn']},
  			"min/LoanSuccess": {include: ["app/controllers/LoanSuccess"], includeLocales: ['zh-cn']},
  			"min/Login": {include: ["app/controllers/Login"], includeLocales: ['zh-cn']},
  			"min/LoginFrame": {include: ["app/controllers/LoginFrame"], includeLocales: ['zh-cn']},
  			"min/ManageTeam": {include: ["app/controllers/ManageTeam"], includeLocales: ['zh-cn']},
  			"min/Message": {include: ["app/controllers/Message"], includeLocales: ['zh-cn']},
  			"min/MoneyIn": {include: ["app/controllers/MoneyIn"], includeLocales: ['zh-cn']},
  			"min/MoneyOut": {include: ["app/controllers/MoneyOut"], includeLocales: ['zh-cn']},
  			"min/MoreInfo": {include: ["app/controllers/MoreInfo"], includeLocales: ['zh-cn']},
  			"min/MyTenders": {include: ["app/controllers/MyTenders"], includeLocales: ['zh-cn']},
  			"min/NewGuidelines": {include: ["app/controllers/NewGuidelines"], includeLocales: ['zh-cn']},
  			"min/P1": {include: ["app/controllers/P1"], includeLocales: ['zh-cn']},
  			"min/P2": {include: ["app/controllers/P2"], includeLocales: ['zh-cn']},
  			"min/P3": {include: ["app/controllers/P3"], includeLocales: ['zh-cn']},
  			"min/P4": {include: ["app/controllers/P4"], includeLocales: ['zh-cn']},
  			"min/Partners": {include: ["app/controllers/Partners"], includeLocales: ['zh-cn']},
  			"min/PlannerAsset": {include: ["app/controllers/PlannerAsset"], includeLocales: ['zh-cn']},
  			"min/PlannerDeal": {include: ["app/controllers/PlannerDeal"], includeLocales: ['zh-cn']},
  			"min/PlannerEntrust": {include: ["app/controllers/PlannerEntrust"], includeLocales: ['zh-cn']},
  			"min/PlatformAdvan": {include: ["app/controllers/PlatformAdvan"], includeLocales: ['zh-cn']},
  			"min/Popularize": {include: ["app/controllers/Popularize"], includeLocales: ['zh-cn']},
  			"min/Prompt": {include: ["app/controllers/Prompt"], includeLocales: ['zh-cn']},
  			"min/PromptTrade": {include: ["app/controllers/PromptTrade"], includeLocales: ['zh-cn']},
  			"min/QuestionFind": {include: ["app/controllers/QuestionFind"], includeLocales: ['zh-cn']},
  			"min/Recharge": {include: ["app/controllers/Recharge"], includeLocales: ['zh-cn']},
  			"min/Register": {include: ["app/controllers/Register"], includeLocales: ['zh-cn']},
  			"min/RegisterSuccess": {include: ["app/controllers/RegisterSuccess"], includeLocales: ['zh-cn']},
  			"min/RepaymentInquiry": {include: ["app/controllers/RepaymentInquiry"], includeLocales: ['zh-cn']},
  			"min/RepaymentPayback": {include: ["app/controllers/RepaymentPayback"], includeLocales: ['zh-cn']},
  			"min/RepaymentSuccess": {include: ["app/controllers/RepaymentSuccess"], includeLocales: ['zh-cn']},
  			"min/RWInquiry": {include: ["app/controllers/RWInquiry"], includeLocales: ['zh-cn']},
  			"min/Safeguards": {include: ["app/controllers/Safeguards"], includeLocales: ['zh-cn']},
  			"min/Security": {include: ["app/controllers/Security"], includeLocales: ['zh-cn']},
  			"min/shareholder": {include: ["app/controllers/shareholder"], includeLocales: ['zh-cn']},
  			"min/StockAssets": {include: ["app/controllers/StockAssets"], includeLocales: ['zh-cn']},
  			"min/StockBuy": {include: ["app/controllers/StockBuy"], includeLocales: ['zh-cn']},
  			"min/StockDeal": {include: ["app/controllers/StockDeal"], includeLocales: ['zh-cn']},
  			"min/StockEntrust": {include: ["app/controllers/StockEntrust"], includeLocales: ['zh-cn']},
  			"min/StockSell": {include: ["app/controllers/StockSell"], includeLocales: ['zh-cn']},
  			"min/StockWithdrwal": {include: ["app/controllers/StockWithdrwal"], includeLocales: ['zh-cn']},
  			"min/Tender": {include: ["app/controllers/Tender"], includeLocales: ['zh-cn']},
  			"min/TenderDetail": {include: ["app/controllers/TenderDetail"], includeLocales: ['zh-cn']},
  			"min/TPasswordQuestion": {include: ["app/controllers/TPasswordQuestion"], includeLocales: ['zh-cn']},
  			"min/TPasswordQuestionUpdate": {include: ["app/controllers/TPasswordQuestionUpdate"], includeLocales: ['zh-cn']},
  			"min/TPasswordSet": {include: ["app/controllers/TPasswordSet"], includeLocales: ['zh-cn']},
  			"min/TradePwdFind1": {include: ["app/controllers/TradePwdFind1"], includeLocales: ['zh-cn']},
  			"min/TradePwdFind2": {include: ["app/controllers/TradePwdFind2"], includeLocales: ['zh-cn']},
  			"min/TradePwdFindSelect": {include: ["app/controllers/TradePwdFindSelect"], includeLocales: ['zh-cn']},
  			"min/UploadID": {include: ["app/controllers/UploadID"], includeLocales: ['zh-cn']},
  			"min/WebSecurity": {include: ["app/controllers/WebSecurity"], includeLocales: ['zh-cn']},
  			"min/Withdraw": {include: ["app/controllers/Withdraw"], includeLocales: ['zh-cn']}
        },
        
        staticHasFeatures: {
			'dojo-trace-api': 0,
			'dojo-log-api': 0,
			'dojo-publish-privates': 0,
			'dojo-sync-loader': 0,
			'dojo-xhr-factory': 0,
			'dojo-test-sniff': 0
//			'dojo-v1x-i18n-Api': 0
		}
    };
})();