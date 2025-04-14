module.exports = {
    chainWebpack: (config) => {
        config.plugin('html').tap((args) => {
            args[0].title = '害你在心口难开';
            return args;
        });
    }
};