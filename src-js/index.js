window.deps = {
    'react' : require('react'),
    'react-dom' : require('react-dom'),
    'react-dom-server': require('react-dom/server'),
    'bip39': require('bip39')
    //'zcoin-lib': require('bitcore-lib-zcoin')
};

window.React = window.deps['react'];
window.ReactDOM = window.deps['react-dom'];
window.ReactDOMServer = window.deps['react-dom-server'];
