<template>
    <div>
        <table class="text-left m-1" style="border-collapse:collapse">
            <thead>
            <tr>
                <th class="py-2 px-2 bg-grey-lighter font-sans font-regular text-sm text-grey border-b border-grey-light">Name</th>
                <th class="py-2 px-2 bg-grey-lighter font-sans font-regular text-sm text-grey border-b border-grey-light">Description</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="report in reports" class="hover:bg-blue-lightest" v-bind:key="report.name">
                <td class="py-1 px-1 border-b font-sans text-sm border-grey-light text-left">
                    <router-link :to="`/report/${report.name}`" >{{report.name}}</router-link>
                </td>
                <td class="py-1 px-1 border-b font-sans text-sm border-grey-light text-left">{{report.description}}</td>
            </tr>
            </tbody>
        </table>
    </div>
    
</template>

<script>
import axios from 'axios'

export default {
  name: 'ReportList',
  data () {
    return {
      reports: [],
      loading: true,
      errored: false
    }
  },
  mounted () {
    axios
      .get('http://127.0.0.1:18080/report')
      .then(response => {
        this.reports = response.data
      })
      .catch(error => {
        this.errored = true
      })
      .finally(() => this.loading = false)
  }
}
</script>