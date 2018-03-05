const webpack = require('webpack');
const path = require('path');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin');

module.exports = {
    entry: "./index.js",
    output: {
        path: path.resolve(__dirname, '../resources/static/assets/scripts'),
        filename: "qxcmp.js"
    },

    devtool: "source-map",

    plugins: [
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            "window.jQuery": "jquery"
        }),
        new UglifyJSPlugin({
            parallel: {
                cache: true,
                workers: 4
            },
            extractComments: true,
            sourceMap: true
        }),
        new ExtractTextPlugin("qxcmp.css"),
        new OptimizeCssAssetsPlugin({
            cssProcessor: require("cssnano"),
            cssProcessorOptions: {
                discardComments: {
                    removeAll: true
                }
            },
            canPrint: true
        })
    ],

    module: {
        rules: [{
            test: /\.js|jsx$/,
            exclude: /(node_modules|bower_components)/,
            use: [{
                loader: "babel-loader",
                options: {
                    presets: ['es2015']
                }
            }]
        }, {
            test: /\.css$/,
            use: ExtractTextPlugin.extract({
                use: {
                    loader: "css-loader",
                    options: {
                        minimize: true
                    }
                }
            })
        }, {
            test: /\.(png|jpg|gif)$/,
            use: [{
                loader: "url-loader",
                options: {
                    limit: 8192,
                    publicPath: "/assets/scripts/",
                    name: "[name].[ext]"
                }
            }]
        }, {
            test: /\.(ttf|eot|svg|woff(2)?)(\?[a-z0-9=&.]+)?$/,
            use: [{
                loader: "file-loader",
                options: {
                    publicPath: "/assets/scripts/",
                    name: "[name].[ext]"
                }
            }]
        }, {
            test: /\.scss$/,
            use: ExtractTextPlugin.extract([{
                loader: "css-loader",
                options: {
                    minimize: true
                }
            }, "sass-loader"])
        }, {
            test: require.resolve('jquery'),
            use: [{
                loader: 'expose-loader',
                options: '$'
            }]
        }]
    }
};