<template>
  <div
    class="map-zoom-box"
  >
    <div style="margin-bottom: 0.5em;">
      <el-button
        :style="{'background': buttonTextColor ? 'none' : '', 'opacity': buttonTextColor ? '0.75': '', 'color': buttonTextColor, 'borderColor': buttonTextColor}"
        size="mini"
        icon="el-icon-plus"
        circle
        @click="callParent('roamMap', true)"
      />
    </div>

    <div style="margin-bottom: 0.5em;">
      <el-button
        :style="{'background': buttonTextColor ? 'none' : '', 'opacity': buttonTextColor ? '0.75': '', 'color': buttonTextColor, 'borderColor': buttonTextColor}"
        size="mini"
        icon="el-icon-refresh"
        circle
        @click="callParent('resetZoom')"
      />
    </div>

    <div>
      <el-button
        :style="{'background': buttonTextColor ? 'none' : '', 'opacity': buttonTextColor ? '0.75': '', 'color': buttonTextColor, 'borderColor': buttonTextColor}"
        size="mini"
        icon="el-icon-minus"
        circle
        @click="callParent('roamMap', false)"
      />
    </div>

  </div>
</template>

<script>
export default {
  name: 'MapController',
  props: {
    chart: {
      type: Object,
      required: true
    },
    buttonTextColor: {
      type: String,
      default: null
    }
  },
  data() {
    return {

    }
  },
  computed: {
    yaxis() {
      return JSON.parse(this.chart.yaxis)
    },
    layerOption() {
      return this.yaxis.map(this.buildOption)
    },
    customAttr() {
      const attr = JSON.parse(this.chart.customAttr)
      if (!attr.currentSeriesId || !this.layerOption.some(item => item.id === attr.currentSeriesId)) {
        attr.currentSeriesId = this.layerOption[0].id
      }
      return attr
    }
  },
  created() {
    this.init()
  },
  methods: {
    buildOption({ id, name }) {
      return ({ id, name })
    },
    changeSeriesId(val) {
      this.chart.customAttr = JSON.stringify(this.customAttr)
    },
    callParent(methodName, param) {
      this.$emit(methodName, param)
    },
    init() {

    }
  }
}
</script>

<style lang="scss" scoped>

</style>
