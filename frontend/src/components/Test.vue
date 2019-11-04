<template>
<div>
    <div>This is my test widget</div>
    <div>{{ message }}</div>
    <input type="text" placeholder="type here" v-model="message">
    <div>
        <form>
            <div>a little text</div>
        <div>
            <label for="firstInput">first input is here: </label>
            <input id="firstInput" placeholder="first" v-model="param.first"/>
        </div>
        <div>
            <label for="secondInput">second input is here: </label>
            <input id="secondInput" placeholder="secondInput" v-model="param.second"/>
        </div>
        <div>
            <button v-on:click="printParams">print params</button>
        </div>
    </form>
    </div>
    
</div>
</template>

<script>
export default {
  name: 'Test',
  data() {
      return {
          message: 'Initial message',
          param: {}
      }
  },
  methods: {
     printParams: function() {
         console.warn(JSON.stringify(this.param))
     } 
  }
}
</script>
