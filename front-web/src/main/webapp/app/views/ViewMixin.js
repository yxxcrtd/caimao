/**
 * Created by luoqi on 2014/12/8.
 */
define([
    'dojo/_base/declare',
    'dojo/_base/config',
    'app/common/Global',
    'app/ux/GenericTooltip'
], function(declare, Config, Global, Tooltip){
    return declare([], {
        baseUrl: Global.baseUrl,
        dojoUrl: Config.baseUrl,
        QQ: Config.QQ,
        hotline: Config.hotline,
        setValues: function(values) {
            for (var key in values) {
                this.set(key, values[key]);
            }
            if (this.afterSet) {
                this.afterSet(values);
            }
        },
        hide: function() {
            this.domNode.style.display = 'none';
            Tooltip.hide();

        },
        show: function() {
            this.domNode.style.display = 'block';
        },

        isValid: function() {
            return true;
        }
    });
});