<template>
    <div class="right-container">
        <h3>
            Right组件
        </h3>
        <hr/>
        <Count :init="6"></Count>
    </div>
</template>
<script>
    export default {

    }
</script>
<style lang="less">
    .right-container {
        padding: 0 20px 20px;
        background-color: aqua;
        min-height: 250px;
        flex: 1;
    }
</style>