# zcoin-wallet

Zcoin wallet on web

## Overview

The features we going to implement is listed below

- Create wallet by mnemonic
- Restore wallet by mnemonic
- Sign Transaction
- Send Transaction
- View transaction history
- Mint/Spend zerocoin
- Multi signature
- Restore wallet by private-key (wif)

## Setup

Install npm modules run:

    yarn

To get an interactive development environment run:

    yarn start

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    yarn clean

To create a production build run:

    yarn build

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

## License

Copyright © 2014 Supakorn Warodom

Distributed under the Eclipse Public License either version 1.0
